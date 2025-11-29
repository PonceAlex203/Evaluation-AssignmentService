package microservice.ProcessEvaluation.Services;

import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.Comunication.Info.NotificationEvent;
import microservice.Comunication.Publisher.MessageNotification;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Repositories.DraftRepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DraftService extends ProcessService<Draft, DraftRepository>{

    private final FormatAService formatAService;

    public DraftService(DraftRepository repository, ProcessFactory processFactory, FormatAService formatAService, Publisher pPublisher) {
        super(repository, processFactory, pPublisher);
        this.formatAService = formatAService;
    }

    protected Draft searchUnassignedByDwIdAndDeptId(Long pDegreeWorkId, Long pDepartmentHeadId){
        return repository.findUnassignedDraft(pDepartmentHeadId,pDegreeWorkId).
                orElseThrow(()->new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    @Override
    protected Draft searchPendingAssignment(Long pDegreeWorkId) {
        return repository.findAllBy(pDegreeWorkId, List.of(
                EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED,
                EnumDegreeWorkStateType.DRAFT_SUBMITTED
                )).
                orElseThrow(()->new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    public List<Draft> getPendingEvaluate(Long pEvaluatorId){
        return repository.findAllBy(pEvaluatorId, EnumProcessStatus.PENDING);
    }
    public List<Draft> getPendingAssignment(Long pDepartmentHead){
        return repository.findPendingAssignmentBy(pDepartmentHead);
    }

    @Override
    protected Draft searchAssignedPending(Long pDegreeWorkId, Long pEvaluatorId) {
        return repository.findEvaluator(pDegreeWorkId,pEvaluatorId,EnumProcessStatus.PENDING).
                orElseThrow(()->new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }
    @Override
    protected void executeUpload(Draft vCurrentProcess) {
            vCurrentProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_SUBMITTED);
    }

    @Override
    public Draft getApproved(Long pDegreeWorkId) {
        return repository.findByDegreeWorkAndGeneralStatus(pDegreeWorkId,EnumDegreeWorkStateType.DRAFT_APPROVED).
                orElse(null);
    }

    @Override
    protected void validateRequirements(Draft pCurrentProcess) {
        if(pCurrentProcess.isExpired())
            throw new ProcessException(EnumTypeExceptions.EXPIRED_TIME);
    }
    @Override
    protected void validateBeforeCreate(Draft pNewProcess) {
        if(formatAService.getApproved(pNewProcess.getDegreeworkId()) == null)
            throw new ProcessException(EnumTypeExceptions.PREVIOUS_PROCESS_NOT_APPROVED);
    }

    @Override
    protected void executeEvaluation(Draft pProcess, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment) {
        if(pProcess.isEvaluator(pIdEvaluator))
            pProcess.getEvaluation().evaluate(pNewStatus,pComment);
        else
            pProcess.getEvaluation2().evaluate(pNewStatus,pComment);
        pProcess.setGeneralStatus(EnumDegreeWorkStateType.FIRST_DRAFT_JURY_EVALUATION);
        updateGeneralStatus(pProcess);
    }

    @Override
    protected void executeAssignment(Draft pProcess, Long pIdEvaluator) {
        Evaluation vNewEvaluation = new Evaluation();
        vNewEvaluation.setEvaluatorId(pIdEvaluator);
        if(!pProcess.isAssigned()){
            pProcess.setEvaluation(vNewEvaluation);
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED);
        }
        else {
            pProcess.setEvaluation2(vNewEvaluation);
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_JURY_ASSIGNED);
        }
    }

    private void updateGeneralStatus(Draft pProcess){
        if(!pProcess.isBothEvaluated())
            return;
        if(pProcess.isApproved() && pProcess.isApproved2())
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_APPROVED);
        else if(pProcess.getEvaluation().isRejected() || pProcess.getEvaluation2().isRejected())
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_REJECTED);
    }

    public Draft assignedEvaluators(Long pIdDw, Long pDepartmentHeadId, Long pId1, Long pId2){
        if(pId1 == pId2)
            throw new ProcessException(EnumTypeExceptions.IDENTICAL_EVALUATORS_IDS);
        Draft vCurrentDraft = searchUnassignedByDwIdAndDeptId(pIdDw,pDepartmentHeadId);
        vCurrentDraft.setEvaluation(new Evaluation(pId1));
        vCurrentDraft.setEvaluation2(new Evaluation(pId2));
        vCurrentDraft.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_JURY_ASSIGNED);
        Draft vUpdatedDraft = repository.save(vCurrentDraft);
        //
        sendDoubleAssignedNotification(vUpdatedDraft);
        sendAssignmentStatusChangeEvent(vUpdatedDraft);
        //
        return vUpdatedDraft;
    }
    private void sendDoubleAssignedNotification(Draft pProcess){
        publisher.sendToNotificationQueue(new NotificationEvent(pProcess.getDegreeworkId()
                , MessageNotification.processAssigneds(pProcess)));
    }


}
