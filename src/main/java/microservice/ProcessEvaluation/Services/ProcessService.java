package microservice.ProcessEvaluation.Services;

import microservice.Comunication.Info.EvaluationEvent;
import microservice.Comunication.Info.NotificationEvent;
import microservice.Comunication.Publisher.MessageNotification;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Repositories.ProcessRepository;
import microservice.SecurityComponent.ProcessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Abstract service that defines the core logic for process management,
 * including creation, evaluation, assignment, and notifications.
 *
 * @param <T> the process type
 * @param <R> the repository type
 */
@Transactional
public abstract class ProcessService<T extends BaseProcess, R extends ProcessRepository<T, R>> {

    protected final R repository;
    protected final ProcessFactory processFactory;
    protected final Publisher publisher;

    /**
     * Constructs a process service with its dependencies.
     *
     * @param repository the process repository
     * @param processFactory the process factory
     * @param pPublisher event publisher
     */
    @Autowired
    public ProcessService(R repository, ProcessFactory processFactory, Publisher pPublisher) {
        this.repository = repository;
        this.processFactory = processFactory;
        this.publisher = pPublisher;
    }

    /**
     * Retrieves all processes.
     *
     * @return list of processes
     */
    public List<T> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves a process by degree work ID.
     *
     * @param pDegreeWorkId degree work ID
     * @return process or null if not found
     */
    public T getBy(Long pDegreeWorkId) {
        return repository.findBy(pDegreeWorkId).orElse(null);
    }

    /**
     * Retrieves processes pending evaluation.
     *
     * @return list of pending processes
     */
    public List<T> getPendingEvaluations() {
        return repository.findByEvaluationStatus(EnumProcessStatus.PENDING);
    }

    /**
     * Retrieves the approved version of a process.
     *
     * @param pDegreeWorkId degree work ID
     * @return approved process
     */
    public abstract T getApproved(Long pDegreeWorkId);

    /**
     * Searches for a process pending evaluation for a specific evaluator.
     */
    protected abstract T searchPendingEvaluate(Long pDegreeWorkId, Long pEvaluatorId);

    /** Validates rules before creating a process. */
    protected abstract void validateBeforeCreate(T pNewProcess);

    /** Validates process requirements after an action. */
    protected abstract void validateRequirements(T pCurrentProcess);

    /** Executes evaluation logic on a process. */
    protected abstract void executeEvaluation(T pProcess, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment);

    /** Executes evaluator assignment. */
    protected abstract void executeAssignment(T pProcess, Long pIdEvaluator);

    /** Executes upload actions on a process. */
    protected abstract void executeUpload(T vCurrentProcess);

    /**
     * Finds a process or throws an exception if not found.
     *
     * @param pDegreeworkId degree work ID
     * @return process
     */
    public T searchBy(Long pDegreeworkId) {
        return repository.findBy(pDegreeworkId)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Finds a rejected process.
     *
     * @param pIdDw degree work ID
     * @return rejected process
     */
    protected T searchRejectedProcess(Long pIdDw) {
        return repository.findByDegreeWorkIdAndEvaluationStatus(pIdDw, EnumProcessStatus.REJECTED)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NON_MODIFICABLE_PROCESS));
    }

    /**
     * Finds a process pending assignment.
     *
     * @param pDegreeWorkId degree work ID
     * @return process pending assignment
     */
    protected T searchPendingAssignment(Long pDegreeWorkId) {
        return repository.findUnassignedBy(pDegreeWorkId)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Saves a new process after validation.
     *
     * @param pNewProcess process to save
     * @return saved process
     */
    public T save(T pNewProcess) {
        checkExistenceProcess(pNewProcess);
        validateBeforeCreate(pNewProcess);
        T vNewProcess = repository.save(pNewProcess);
        sendSubmittedNotification(vNewProcess);
        sendEvaluationStatusChangeEvent(vNewProcess);
        return vNewProcess;
    }

    /**
     * Reuploads a previously rejected process.
     *
     * @param pReUploadProcess new process data
     * @return updated process
     */
    public T reUploadProcess(T pReUploadProcess) {
        T vCurrentProcess = searchRejectedProcess(pReUploadProcess.getDegreeworkId());
        validateRequirements(vCurrentProcess);
        vCurrentProcess.setUrl(pReUploadProcess.getUrl());
        executeUpload(vCurrentProcess);
        T vNewProcess = repository.save(vCurrentProcess);
        sendUpdatedNotification(vNewProcess);
        sendEvaluationStatusChangeEvent(vNewProcess);
        return vNewProcess;
    }

    /**
     * Evaluates a process.
     *
     * @param pIdDw degree work ID
     * @param pIdEvaluator evaluator ID
     * @param pNewStatus new status
     * @param pComment evaluation comment
     * @return evaluated process
     */
    public T evaluateProcess(Long pIdDw, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment) {
        T vProcess = searchPendingEvaluate(pIdDw, pIdEvaluator);
        executeEvaluation(vProcess, pIdEvaluator, pNewStatus, pComment);
        validateRequirements(vProcess);
        T vEvaluatedProcess = repository.save(vProcess);
        sendEvaluationStatusChangeEvent(vEvaluatedProcess);
        sendEvaluatedNotification(vEvaluatedProcess);
        return vEvaluatedProcess;
    }

    /**
     * Assigns an evaluator to a process.
     *
     * @param pDwId degree work ID
     * @param pIdEvaluator evaluator ID
     * @return updated process
     */
    public T assignmentEvaluator(Long pDwId, Long pIdEvaluator) {
        T vProcess = searchPendingAssignment(pDwId);
        executeAssignment(vProcess, pIdEvaluator);
        T vUpdatedProcess = repository.save(vProcess);
        sendAssignmentStatusChangeEvent(vProcess);
        sendAssignedNotification(vUpdatedProcess);
        return vUpdatedProcess;
    }

    // ------------------- Event and Notification senders -------------------

    /** Sends notification when a process is updated. */
    protected void sendUpdatedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(
                pProcess.getDegreeworkId(),
                MessageNotification.processUpdated(pProcess)));
    }

    /** Sends notification when a process is evaluated. */
    protected void sendEvaluatedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(
                pProcess.getDegreeworkId(),
                MessageNotification.processEvaluated(pProcess)));
    }

    /** Sends notification when a process is submitted. */
    protected void sendSubmittedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(
                pProcess.getDegreeworkId(),
                MessageNotification.processSubmitted(pProcess)));
    }

    /** Sends notification when a process is assigned. */
    protected void sendAssignedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(
                pProcess.getDegreeworkId(),
                MessageNotification.processAssigned(pProcess)));
    }

    /** Emits an event when process evaluation status changes. */
    protected void sendEvaluationStatusChangeEvent(T pProcess) {
        publisher.sendToModifierQueue(new EvaluationEvent(
                pProcess.getDegreeworkId(),
                pProcess.getGeneralStatus()));
    }

    /** Emits an event when a process assignment status changes. */
    protected void sendAssignmentStatusChangeEvent(T pProcess) {
        publisher.sendToModifierQueue(new EvaluationEvent(
                pProcess.getDegreeworkId(),
                pProcess.getGeneralStatus()));
    }

    /** Checks if a process already exists and throws an exception otherwise. */
    private void checkExistenceProcess(T pNewProcess) {
        if (getBy(pNewProcess.getDegreeworkId()) != null)
            throw new ProcessException(EnumTypeExceptions.EXISTING_ID);
    }
}




