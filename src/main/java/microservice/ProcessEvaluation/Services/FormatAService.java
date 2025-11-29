package microservice.ProcessEvaluation.Services;

import microservice.Comunication.Info.EnumDegreeWorkStateType;
import microservice.Comunication.Info.EvaluationEvent;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumAssignmentStatus;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Repositories.FormatARepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormatAService extends ProcessService<FormatA,FormatARepository> {
    private final byte maxAttempts = 3;

    @Autowired
    public FormatAService(FormatARepository repository, ProcessFactory processFactory, Publisher pPublisher) {
        super(repository, processFactory, pPublisher);
    }

    @Override
    protected void validateRequirements(FormatA pCurrentProcess) {
        if(pCurrentProcess.getAttempts() >= maxAttempts) {
            pCurrentProcess.setGeneralEvaluationStatus(EnumProcessStatus.FAILED);
        }
    }

    @Override
    protected void validateEvaluator(FormatA pProcess, Long pEvaluatorId) {
        if(pEvaluatorId == null)
            throw new ProcessException(EnumTypeExceptions.NULL_PARAMETER);
        if(pEvaluatorId!= 1) //Solo 1 coord, su id es 1.
            throw new ProcessException(EnumTypeExceptions.NOT_ASSIGNED_TO_SELECTED_PROCESS);
    }

    @Override
    protected void executeEvaluation(FormatA pProcess, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment) {
        pProcess.getEvaluation().evaluate(pNewStatus,pComment);
        pProcess.setGeneralEvaluationStatus(pNewStatus);//Se asigna el mismo estado que a la evaluacion ya que es 1 sola.
    }

    @Override
    protected void validateCanBeEvaluated(FormatA pProcess) {
        this.validateCurrentStatus(pProcess);
        if(pProcess.getEvaluation() == null)
            throw new ProcessException(EnumTypeExceptions.NOT_ASSIGNED_PROCESS);
        if(pProcess.getGeneralEvaluationStatus()!=EnumProcessStatus.PENDING)
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_PENDING);
    }

    @Override
    protected void validateBeforeAssigning(FormatA pProcess,Long pIdEvaluator) {
        if(pProcess.isFullAssigned())
            throw new ProcessException(EnumTypeExceptions.ALREADY_ASSIGNED);
    }

    @Override
    protected void executeAssignment(FormatA pProcess, Long pIdEvaluator) {
        pProcess.setEvaluator(pIdEvaluator);
        pProcess.setAssignmentStatus(EnumAssignmentStatus.ASSIGNED);
    }

    @Override
    protected void validateBeforeCreate(FormatA pNewProcess) {
        if(pNewProcess == null)
            throw new ProcessException(EnumTypeExceptions.NOT_FOUND);
    }

    @Override
    protected void sendEvaluationStatusChangeEvent(FormatA pProcess) {
        EnumDegreeWorkStateType vNewStatus;
        switch (pProcess.getGeneralEvaluationStatus()){
            case PENDING -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_SUBMITTED;
            case APPROVED -> vNewStatus = EnumDegreeWorkStateType.DRAFT;
            case REJECTED -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_REJECTED;
            case FAILED -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_FAILED;
            default -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A;
        }
        publisher.sendToModifierQueue(new EvaluationEvent(pProcess.getDegreeworkId(), vNewStatus));
    }
}
