package Evaluation_AssignmentService.ProcessEvaluation.ProcessService;

import Evaluation_AssignmentService.Comunication.Info.EnumDegreeWorkStateType;
import Evaluation_AssignmentService.Comunication.Info.EvaluationEvent;
import Evaluation_AssignmentService.Comunication.Info.NotificationEvent;
import Evaluation_AssignmentService.Comunication.Publisher.MessageNotification;
import Evaluation_AssignmentService.Comunication.Publisher.Publisher;
import Evaluation_AssignmentService.ProcessEvaluation.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.BaseProcess;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessRepository.ProcessRepository;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Abstract service that defines common operations and validation logic
 * for all process types in the system.
 *
 * @param <T> the type of process extending {@link BaseProcess}
 */
@Transactional
public abstract class ProcessService<T extends BaseProcess> {
    protected final ProcessRepository<T> repository;
    protected final ProcessFactory processFactory;
    protected final Publisher publisher;

    @Autowired
    public ProcessService(ProcessRepository<T> repository, ProcessFactory processFactory, Publisher pPublisher) {
        this.repository = repository;
        this.processFactory = processFactory;
        this.publisher = pPublisher;
    }

    /**
     * Finds a process by its ID.
     *
     * @param pId the process ID
     * @return the process if found, null otherwise
     */
    protected T findById(Long pId){ return repository.findById(pId).orElse(null); }

    /**
     * Retrieves all processes with a given status.
     *
     * @param pStatus the process status
     * @return a list of processes with the given status
     */
    public List<T> findByStatus(EnumProcessStatus pStatus) {
        return repository.findByStatus(pStatus);
    }

    /**
     * Retrieves all processes from the repository.
     *
     * @return a list of all processes
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Saves a new process after validation.
     *
     * @param pNewProcess the process to save
     * @return the saved process
     */
    public T save(T pNewProcess) {
        if(this.extractByDegreeWorkId(pNewProcess.getDegreeworkId()) != null)
            throw new ProcessException(EnumTypeExceptions.EXISTING_ID);
        validateBeforeCreate(pNewProcess);
        T vNewProcess = repository.save(pNewProcess);
        //
        sendSubmittedNotification(vNewProcess);
        sendStatusChangeEvent(vNewProcess);
        //
        return vNewProcess;
    }

    /**
     * Reuploads a process if it was rejected, after validation.
     *
     * @param pReUploadProcess the updated process
     * @return the reuploaded process
     */
    public T reUploadProcess(T pReUploadProcess){
        T vCurrentProcess = this.findByDegreeWorkId(pReUploadProcess.getDegreeworkId());
        validateCanBeResubmitted(vCurrentProcess);
        validateRequirements(vCurrentProcess);
        vCurrentProcess.setUrl(pReUploadProcess.getUrl());
        vCurrentProcess.setStatus(EnumProcessStatus.PENDING);
        repository.save(vCurrentProcess);
        //
        sendUpdatedNotification(vCurrentProcess);
        sendStatusChangeEvent(vCurrentProcess);
        //
        return vCurrentProcess;
    }

    /**
     * Evaluates a process, updating its status and comments.
     *
     * @param pId the process ID
     * @param pComment the evaluator comment
     * @param pNewStatus the new status
     * @return the evaluated process
     */
    public T evaluateProcess(Long pId, String pComment, EnumProcessStatus pNewStatus) {
        T vCurrentProcess = this.findByDegreeWorkId(pId);
        validateCanBeEvaluated(vCurrentProcess);
        validateNewStatus(pNewStatus);
        vCurrentProcess.setComments(pComment);
        vCurrentProcess.setStatus(pNewStatus);
        validateRequirements(vCurrentProcess);
        repository.save(vCurrentProcess);
        //
        sendEvaluatedNotification(vCurrentProcess);
        sendStatusChangeEvent(vCurrentProcess);
        //
        return vCurrentProcess;
    }
    /**
     * Validates that the process can be evaluated based on its current status.
     * A process can only be evaluated if it is in PENDING status.
     *
     * @param pProcess Process instance to validate
     * @throws ProcessException if the process cannot be evaluated due to its current status
     */
    private void validateCanBeEvaluated(T pProcess){
        validateCurrentStatus(pProcess);
        if(!pProcess.getStatus().equals(EnumProcessStatus.PENDING))
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_PENDING);
    }

    /**
     * Validates that the process can be resubmitted based on its current status.
     * A process can only be resubmitted if it is in REJECTED status.
     *
     * @param pProcess Process instance to validate
     * @throws ProcessException if the process cannot be resubmitted due to its current status
     */
    private void validateCanBeResubmitted(T pProcess){
        validateCurrentStatus(pProcess);
        if(!pProcess.getStatus().equals(EnumProcessStatus.REJECTED))
            throw new ProcessException(EnumTypeExceptions.NON_MODIFICABLE_PROCESS);
    }

    /**
     * Validates the current process status before performing actions.
     * Processes in FAILED or APPROVED status cannot be modified or evaluated.
     *
     * @param pCurrentProcess Process instance to verify
     * @throws ProcessException if the process is FAILED or APPROVED
     */
    private void validateCurrentStatus(T pCurrentProcess){
        if(pCurrentProcess.getStatus().equals(EnumProcessStatus.FAILED))
            throw new ProcessException(EnumTypeExceptions.PROCESS_FAILED);
        if(pCurrentProcess.getStatus().equals(EnumProcessStatus.APPROVED))
            throw new ProcessException(EnumTypeExceptions.PROCESS_APPROVED);
    }

    /**
     * Ensures the new status is valid for update.
     */
    private void validateNewStatus(EnumProcessStatus pNewStatus){
        if(pNewStatus == null || pNewStatus.equals(EnumProcessStatus.PENDING))
            throw new ProcessException(EnumTypeExceptions.INVALID_NEW_STATUS);
        if(pNewStatus.equals(EnumProcessStatus.FAILED))
            throw new ProcessException(EnumTypeExceptions.INVALID_NEW_STATUS);
    }

    /**
     * Finds a process by degree work ID.
     *
     * @param pDegreeworkId the degree work ID
     * @return the found process
     */
    public T findByDegreeWorkId(Long pDegreeworkId) {
        return repository.findByDegreeworkId(pDegreeworkId)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Extracts a process by degree work ID, returning null if not found.
     */
    public T extractByDegreeWorkId(Long pDegreeWorkId){
        return repository.findByDegreeworkId(pDegreeWorkId).orElse(null);
    }


    private void sendUpdatedNotification(T pProcess){
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                ,MessageNotification.ProcessUpdated(pProcess)));
    }
    private void sendEvaluatedNotification(T pProcess){
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                ,MessageNotification.ProcessEvaluated(pProcess)));
    }
    private void sendSubmittedNotification(T pProcess){
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                ,MessageNotification.ProcessSubmitted(pProcess)));
    }
    protected final void sendStatusChangeEvent(T pProcess, EnumDegreeWorkStateType pNewStatus){
        publisher.sendToModifierQueue(new EvaluationEvent(pProcess.getDegreeworkId(), pNewStatus));
    }
    protected abstract void sendStatusChangeEvent(T pProcess);

    /**
     * Defines validations before creating a process.
     */
    protected abstract void validateBeforeCreate(T pNewProcess);

    /**
     * Defines process-specific requirements validation.
     */
    protected abstract void validateRequirements(T pCurrentProcess);
}



