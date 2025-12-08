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


/**
 * Service that manages the Draft process lifecycle.
 * Handles assignment, evaluation, validation, and status transitions for Draft entities.
 */
@Service
public class DraftService extends ProcessService<Draft, DraftRepository>{

    private final FormatAService formatAService;

    /**
     * Creates a new DraftService using the provided repository and process factory.
     *
     * @param repository Draft repository used for persistence operations
     * @param processFactory factory responsible for creating process instances
     * @param formatAService service used to validate dependencies with Format A
     * @param pPublisher event publisher for notifications and status changes
     */
    public DraftService(DraftRepository repository, ProcessFactory processFactory,
                        FormatAService formatAService, Publisher pPublisher) {
        super(repository, processFactory, pPublisher);
        this.formatAService = formatAService;
    }

    /**
     * Finds a Draft that has no evaluator assigned.
     *
     * @param pDegreeWorkId DegreeWork identifier
     * @return unassigned Draft
     */
    protected Draft searchUnassignedBy(Long pDegreeWorkId){
        return repository.findUnassignedDraft(pDegreeWorkId)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Finds a Draft pending evaluation for the given evaluator.
     *
     * @param pDegreeWorkId DegreeWork identifier
     * @param pEvaluatorId evaluator identifier
     * @return pending Draft
     */
    @Override
    protected Draft searchPendingEvaluate(Long pDegreeWorkId, Long pEvaluatorId) {
        return repository.findPendingEvaluate(pDegreeWorkId,pEvaluatorId, EnumProcessStatus.PENDING)
                .orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Finds a Draft pending evaluator assignment.
     *
     * @param pDegreeWorkId DegreeWork identifier
     * @return Draft pending assignment
     */
    @Override
    protected Draft searchPendingAssignment(Long pDegreeWorkId) {
        return repository.findAllBy(pDegreeWorkId, List.of(
                EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED,
                EnumDegreeWorkStateType.DRAFT_SUBMITTED
        )).orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Retrieves all drafts pending evaluation for the given evaluator.
     *
     * @param pEvaluatorId evaluator identifier
     * @return list of drafts
     */
    public List<Draft> getPendingEvaluate(Long pEvaluatorId){
        return repository.findAllBy(pEvaluatorId, EnumProcessStatus.PENDING);
    }

    /**
     * Retrieves drafts pending evaluator assignment.
     *
     * @return list of drafts
     */
    public List<Draft> getPendingAssignment(){
        return repository.findPendingAssignment();
    }

    /**
     * Updates Draft status when a file upload is executed.
     *
     * @param vCurrentProcess current draft process
     */
    @Override
    protected void executeUpload(Draft vCurrentProcess) {
        vCurrentProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_SUBMITTED);
    }

    /**
     * Returns an approved Draft for the given DegreeWork identifier.
     *
     * @param pDegreeWorkId DegreeWork identifier
     * @return approved Draft or null if not found
     */
    @Override
    public Draft getApproved(Long pDegreeWorkId) {
        return repository.findByDegreeWorkAndGeneralStatus(
                pDegreeWorkId, EnumDegreeWorkStateType.DRAFT_APPROVED
        ).orElse(null);
    }

    /**
     * Validates whether the current Draft meets the process requirements.
     * Throws an exception if the submission is expired.
     *
     * @param pCurrentProcess draft to validate
     */
    @Override
    protected void validateRequirements(Draft pCurrentProcess) {
        if(pCurrentProcess.isExpired())
            throw new ProcessException(EnumTypeExceptions.EXPIRED_TIME);
    }

    /**
     * Validates rules before creating a new Draft process.
     * Ensures that the previous Format A has been approved.
     *
     * @param pNewProcess new draft instance
     */
    @Override
    protected void validateBeforeCreate(Draft pNewProcess) {
        if(formatAService.getApproved(pNewProcess.getDegreeworkId()) == null)
            throw new ProcessException(EnumTypeExceptions.PREVIOUS_PROCESS_NOT_APPROVED);
    }

    /**
     * Executes a Draft evaluation, updating evaluator data and general status.
     *
     * @param pProcess draft being evaluated
     * @param pIdEvaluator evaluator identifier
     * @param pNewStatus new evaluation status
     * @param pComment evaluation comment
     */
    @Override
    protected void executeEvaluation(Draft pProcess, Long pIdEvaluator,
                                     EnumProcessStatus pNewStatus, String pComment) {
        if(pProcess.isEvaluator(pIdEvaluator))
            pProcess.getEvaluation().evaluate(pNewStatus,pComment);
        else
            pProcess.getEvaluation2().evaluate(pNewStatus,pComment);

        pProcess.setGeneralStatus(EnumDegreeWorkStateType.FIRST_DRAFT_JURY_EVALUATION);
        updateGeneralStatus(pProcess);
    }

    /**
     * Assigns an evaluator to the Draft.
     *
     * @param pProcess draft to update
     * @param pEvaluatorId evaluator identifier
     */
    @Override
    protected void executeAssignment(Draft pProcess, Long pEvaluatorId) {
        if(!pProcess.isAssigned()){
            pProcess.setEvaluation(new Evaluation(pEvaluatorId));
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED);
        }
        else {
            validateEvaluator2(pProcess,pEvaluatorId);
            pProcess.setEvaluation2(new Evaluation(pEvaluatorId));
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_JURY_ASSIGNED);
        }
    }

    /**
     * Ensures that the second evaluator is not the same as the first one.
     *
     * @param pProcess draft being validated
     * @param pEvaluator2Id second evaluator id
     */
    private void validateEvaluator2(Draft pProcess, Long pEvaluator2Id){
        if(pProcess.isEvaluator(pEvaluator2Id))
            throw new ProcessException(EnumTypeExceptions.PREVIOUSLY_ASSIGNED);
    }

    /**
     * Updates the general Draft status after both evaluators have submitted their evaluations.
     *
     * @param pProcess draft to update
     */
    private void updateGeneralStatus(Draft pProcess){
        if(!pProcess.isBothEvaluated())
            return;

        if(pProcess.isApproved() && pProcess.isApproved2())
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_APPROVED);
        else if(pProcess.getEvaluation().isRejected() || pProcess.getEvaluation2().isRejected())
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_REJECTED);
    }

    /**
     * Assigns two evaluators simultaneously to an unassigned Draft.
     * Sends notification and status events after assignment.
     *
     * @param pIdDw DegreeWork identifier
     * @param pId1 first evaluator id
     * @param pId2 second evaluator id
     * @return updated Draft
     */
    public Draft assignedEvaluators(Long pIdDw, Long pId1, Long pId2){
        if(pId1 == pId2)
            throw new ProcessException(EnumTypeExceptions.IDENTICAL_EVALUATORS_IDS);

        Draft vCurrentDraft = searchUnassignedBy(pIdDw);
        vCurrentDraft.setEvaluation(new Evaluation(pId1));
        vCurrentDraft.setEvaluation2(new Evaluation(pId2));
        vCurrentDraft.setGeneralStatus(EnumDegreeWorkStateType.DRAFT_JURY_ASSIGNED);

        Draft vUpdatedDraft = repository.save(vCurrentDraft);

        sendDoubleAssignedNotification(vUpdatedDraft);
        sendAssignmentStatusChangeEvent(vUpdatedDraft);

        return vUpdatedDraft;
    }

    /**
     * Sends a notification when two evaluators are assigned to a Draft.
     *
     * @param pProcess draft with assigned evaluators
     */
    private void sendDoubleAssignedNotification(Draft pProcess){
        publisher.sendToNotificationQueue(new NotificationEvent(
                pProcess.getDegreeworkId(),
                MessageNotification.processAssigneds(pProcess)
        ));
    }
}

