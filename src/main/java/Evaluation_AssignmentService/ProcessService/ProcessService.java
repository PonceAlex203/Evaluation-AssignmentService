package Evaluation_AssignmentService.ProcessService;

import Evaluation_AssignmentService.Dto.ProcessDTO;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.ProcessEntity.BaseProcess;
import Evaluation_AssignmentService.ProcessRepository.ProcessRepository;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Abstract service that defines common operations and validation logic
 * for all process types in the system.
 *
 * @param <T> the type of process extending {@link BaseProcess}
 * @param <D> the DTO type extending {@link ProcessDTO}
 */
@Transactional
public abstract class ProcessService<T extends BaseProcess, D extends ProcessDTO> {
    protected final ProcessRepository<T> repository;
    protected final ProcessFactory processFactory;

    @Autowired
    public ProcessService(ProcessRepository<T> repository, ProcessFactory processFactory) {
        this.repository = repository;
        this.processFactory = processFactory;
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
        return repository.save(pNewProcess);
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
        SynchronizeData(vCurrentProcess, pReUploadProcess);
        vCurrentProcess.setStatus(EnumProcessStatus.PENDING);
        return repository.save(vCurrentProcess);
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
        updateInternalData(vCurrentProcess);
        validateRequirements(vCurrentProcess);
        return repository.save(vCurrentProcess);
    }
    private void validateCanBeEvaluated(T pProcess){
        validateCurrentStatus(pProcess);
        if(!pProcess.getStatus().equals(EnumProcessStatus.PENDING))
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_PENDING);
    }
    private void validateCanBeResubmitted(T pProcess){
        validateCurrentStatus(pProcess);
        if(!pProcess.getStatus().equals(EnumProcessStatus.REJECTED))
            throw new ProcessException(EnumTypeExceptions.NON_MODIFICABLE_PROCESS);
    }

    /**
     * Validates the current process status before performing actions.
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
     * Synchronizes data between current and updated processes.
     */
    protected void SynchronizeData(T pCurrentProcess, T pUpdateProcess){
        pCurrentProcess.setUrl(pUpdateProcess.getUrl());
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

    /**
     * Defines custom update behavior for a specific process type.
     */
    protected abstract void updateInternalData(T pUpdateProcess);

    /**
     * Defines validations before creating a process.
     */
    protected abstract void validateBeforeCreate(T pNewProcess);

    /**
     * Defines process-specific requirements validation.
     */
    protected abstract void validateRequirements(T pCurrentProcess);
}



