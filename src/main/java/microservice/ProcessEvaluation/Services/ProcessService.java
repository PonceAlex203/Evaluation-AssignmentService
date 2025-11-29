package microservice.ProcessEvaluation.Services;

import microservice.Comunication.Info.EvaluationEvent;
import microservice.Comunication.Info.NotificationEvent;
import microservice.Comunication.Publisher.MessageNotification;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Repositories.ProcessRepository;
import microservice.SecurityComponent.ProcessException;
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
public abstract class ProcessService<T extends BaseProcess, R extends ProcessRepository<T,R>> {
    protected final R repository;
    protected final ProcessFactory processFactory;
    protected final Publisher publisher;

    @Autowired
    public ProcessService(R repository, ProcessFactory processFactory, Publisher pPublisher) {
        this.repository = repository;
        this.processFactory = processFactory;
        this.publisher = pPublisher;
    }

    //General
    public List<T> getAll() {
        return repository.findAll();
    }
    
    public T searchByDegreeWorkId(Long pDegreeworkId) {
        return repository.findByDegreeWorkId(pDegreeworkId)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    public T getByDegreeWorkId(Long pDegreeWorkId) {
        return repository.findByDegreeWorkId(pDegreeWorkId).orElse(null);
    }

    //Specific

    public abstract T getApprovedByDegreeWorkId(Long pDegreeWorkId);

    public List<T> getPendingEvaluationByEvaluator(Long pEvaluatorId) {
        return repository.findByEvaluatorAndEvaluationStatus(pEvaluatorId,EnumProcessStatus.PENDING);
    }

    protected T searchRejectedProcessByDwId(Long pIdDw) {
        return repository.findByDegreeWorkIdAndEvaluationStatus(pIdDw,EnumProcessStatus.REJECTED).
                orElseThrow(()->new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    protected T searchAssignedPendingEvaluation(Long pDegreeWorkId, Long pEvaluatorId) {
        return repository.findByDegreeWorkEvaluatorAndEvaluationStatus(pDegreeWorkId,pEvaluatorId,EnumProcessStatus.PENDING).
                orElseThrow(()->new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    protected T searchPendingAssignment(Long pDegreeWorkId) {
        return repository.findUnassignedByDegreeWorkId(pDegreeWorkId).
                orElseThrow(()->new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    protected abstract void validateBeforeCreate(T pNewProcess);
    protected abstract void validateRequirements(T pCurrentProcess);

    protected abstract void executeEvaluation(T pProcess, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment);
    protected abstract void executeAssignment(T pProcess, Long pIdEvaluator);
    protected abstract void executeUpload(T vCurrentProcess);

    ////
    private void checkExistenceProcess(T pNewProcess){
        if (this.getByDegreeWorkId(pNewProcess.getDegreeworkId()) != null)
            throw new ProcessException(EnumTypeExceptions.EXISTING_ID);
    }

    public T save(T pNewProcess) {
        checkExistenceProcess(pNewProcess);
        validateBeforeCreate(pNewProcess);
        T vNewProcess = repository.save(pNewProcess);
        //
        sendSubmittedNotification(vNewProcess);
        sendEvaluationStatusChangeEvent(vNewProcess);
        //
        return vNewProcess;
    }

    public T reUploadProcess(T pReUploadProcess) {
        T vCurrentProcess = searchRejectedProcessByDwId(pReUploadProcess.getDegreeworkId());
        validateRequirements(vCurrentProcess);
        vCurrentProcess.setUrl(pReUploadProcess.getUrl());
        executeUpload(vCurrentProcess);
        T vNewProcess = repository.save(vCurrentProcess);
        //
        sendUpdatedNotification(vNewProcess);
        sendEvaluationStatusChangeEvent(vNewProcess);
        //
        return vNewProcess;
    }
    public T evaluateProcess(Long pIdDw, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment){
        T vProcess = searchAssignedPendingEvaluation(pIdDw, pIdEvaluator);
        executeEvaluation(vProcess, pIdEvaluator,pNewStatus, pComment);
        validateRequirements(vProcess);
        T vEvaluatedProcess = repository.save(vProcess);
        //
        sendEvaluationStatusChangeEvent(vEvaluatedProcess);
        sendEvaluatedNotification(vEvaluatedProcess);
        //
        return vEvaluatedProcess;
    }

    public T assignmentEvaluator(Long pDwId, Long pAssigneeId, Long pIdEvaluator) {
        T vProcess = searchPendingAssignment(pDwId);
        executeAssignment(vProcess, pIdEvaluator);
        T vUpdatedProcess = repository.save(vProcess);
        //
        sendAssignmentStatusChangeEvent(vProcess);
        sendAssignedNotification(vUpdatedProcess);
        //
        return vUpdatedProcess;
    }

    protected void sendUpdatedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                , MessageNotification.processUpdated(pProcess)));
    }

    protected void sendEvaluatedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                , MessageNotification.processEvaluated(pProcess)));
    }

    protected void sendSubmittedNotification(T pProcess) {
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                , MessageNotification.processSubmitted(pProcess)));
    }
    protected void sendAssignedNotification(T pProcess){
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                , MessageNotification.processAssigned(pProcess)));
    }

    protected void sendEvaluationStatusChangeEvent(T pProcess){
        publisher.sendToModifierQueue(new EvaluationEvent(pProcess.getDegreeworkId(), pProcess.getGeneralStatus()));
    }
    protected void sendAssignmentStatusChangeEvent(T pProcess){
        publisher.sendToModifierQueue(new EvaluationEvent(pProcess.getDegreeworkId(), pProcess.getGeneralStatus()));
    }

}



