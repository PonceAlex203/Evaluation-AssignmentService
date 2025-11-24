package microservice.ProcessEvaluation.Services;

import microservice.Comunication.Info.EnumDegreeWorkStateType;
import microservice.Comunication.Info.EvaluationEvent;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Repositories.DraftRepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class that manages logic and validations for @Draft processes.
 */
@Service
public class DraftService extends ProcessService<Draft>{

    @Autowired
    private final DraftRepository draftRepository;
    private final FormatAService formatAService;

    public DraftService(DraftRepository repository, ProcessFactory processFactory, FormatAService formatAService, Publisher pPublisher) {
        super(repository, processFactory, pPublisher);
        this.draftRepository = repository;
        this.formatAService = formatAService;
    }

    /**
     * Validates that the corresponding FormatA process exists and is approved
     * before allowing the creation of a new Draft.
     */
    @Override
    protected void validateRequirements(Draft pCurrentProcess) {
        if(pCurrentProcess.isExpired())
            throw new ProcessException(EnumTypeExceptions.EXPIRED_TIME);
    }

    /**
     * Validates that a related FormatA process exists and is approved
     * before allowing the creation of a new Draft.
     *
     * @param pNewProcess the new Draft process to validate
     */
    @Override
    protected void validateBeforeCreate(Draft pNewProcess) {
        FormatA formatA = formatAService.extractByDegreeWorkId(pNewProcess.getDegreeworkId());
        if (formatA == null)
            throw new ProcessException(EnumTypeExceptions.PREVIOUS_PROCESS_NOT_SUMBITTED);
        if(!formatA.getStatus().equals(EnumProcessStatus.APPROVED))
            throw new ProcessException(EnumTypeExceptions.PREVIOUS_PROCESS_NOT_APPROVED);
    }

    @Override
    protected void sendStatusChangeEvent(Draft pProcess) {
        EnumDegreeWorkStateType vNewStatus;
        switch (pProcess.getStatus()){
            case ASSIGNED -> vNewStatus = EnumDegreeWorkStateType.JURY_ASSIGNED;
            case PENDING -> vNewStatus = EnumDegreeWorkStateType.DRAFT_SUBMITTED;
            case PARTIAL -> vNewStatus = EnumDegreeWorkStateType.FIRST_JURY_EVALUATION;
            case APPROVED -> vNewStatus = EnumDegreeWorkStateType.DRAFT_APPROVED;
            case REJECTED -> vNewStatus = EnumDegreeWorkStateType.DRAFT_REJECTED;
            default -> vNewStatus = EnumDegreeWorkStateType.DRAFT;
        }
        publisher.sendToModifierQueue(new EvaluationEvent(pProcess.getDegreeworkId(), vNewStatus));
    }

    public Draft assignedEvaluators(Long pIdDw, Long pId1, Long pId2){
        Draft vCurrentDraft = this.findByDegreeWorkId(pIdDw);
        if(pId1 == pId2)
            throw new ProcessException(EnumTypeExceptions.IDENTICAL_EVALUATORS_IDS);
        if(vCurrentDraft.isAssigned())
            throw new ProcessException(EnumTypeExceptions.ALREADY_ASSIGNED);
        if(vCurrentDraft.isEvaluator(pId1) || vCurrentDraft.isEvaluator(pId2))
            throw new ProcessException(EnumTypeExceptions.PREVIOUSLY_ASSIGNED);
        vCurrentDraft.setEvaluation(new Evaluation(pId1));
        vCurrentDraft.setEvaluation2(new Evaluation(pId2));
        vCurrentDraft.setStatus(EnumProcessStatus.ASSIGNED);
        Draft vUpdatedDraft = this.repository.save(vCurrentDraft);
        sendStatusChangeEvent(vUpdatedDraft);
        return vUpdatedDraft;
    }
    @Override
    protected void validateEvaluator(Draft pProcess, Long pId) {
        if (!pProcess.isEvaluator(pId))
            throw new ProcessException(EnumTypeExceptions.NOT_ASSIGNED_TO_SELECTED_PROCESS);

    }

    @Override
    protected void executeEvaluation(Draft pProcess, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment) {
        if(pIdEvaluator.equals(pProcess.getEvaluation().getEvaluatorId()))
            pProcess.getEvaluation().evaluate(pNewStatus,pComment);
        else
            pProcess.getEvaluation2().evaluate(pNewStatus,pComment);
        pProcess.setStatus(EnumProcessStatus.PARTIAL);
        if(pProcess.getEvaluation().getEvaluationStatus() == EnumProcessStatus.APPROVED
                && pProcess.getEvaluation2().getEvaluationStatus() == EnumProcessStatus.APPROVED)
            pProcess.setStatus(EnumProcessStatus.APPROVED);
        else if(pProcess.getEvaluation().getEvaluationStatus() == EnumProcessStatus.REJECTED
                || pProcess.getEvaluation2().getEvaluationStatus() == EnumProcessStatus.REJECTED)
            pProcess.setStatus(EnumProcessStatus.REJECTED);
    }

    @Override
    protected void validateCanBeEvaluated(Draft pProcess) {
        this.validateCurrentStatus(pProcess);
        if(!pProcess.isBothAssigned())
            throw new ProcessException(EnumTypeExceptions.NOT_ASSIGNED_PROCESS);
        if(pProcess.isExpired())
            throw new ProcessException(EnumTypeExceptions.EXPIRED_TIME);
        if(pProcess.getEvaluation().isEvaluated() && pProcess.getEvaluation2().isEvaluated())
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_PENDING);
    }

    @Override
    protected void validateBeforeAssigning(Draft pProcess,Long pIdEvaluator) {
        if(pProcess.isBothAssigned())
            throw new ProcessException(EnumTypeExceptions.ALREADY_ASSIGNED);
        if(pProcess.isEvaluator(pIdEvaluator))
            throw new ProcessException(EnumTypeExceptions.PREVIOUSLY_ASSIGNED);
    }

    @Override
    protected void executeAssignment(Draft pProcess, Long pIdEvaluator) {
        Evaluation vNewEvaluation = new Evaluation();
        vNewEvaluation.setEvaluatorId(pIdEvaluator);
        if(pProcess.getEvaluation() == null) pProcess.setEvaluation(vNewEvaluation);
        else pProcess.setEvaluation2(vNewEvaluation);
    }
}

