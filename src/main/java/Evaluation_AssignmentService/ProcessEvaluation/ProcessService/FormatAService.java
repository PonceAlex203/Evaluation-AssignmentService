package Evaluation_AssignmentService.ProcessEvaluation.ProcessService;

import Evaluation_AssignmentService.Comunication.Info.EnumDegreeWorkStateType;
import Evaluation_AssignmentService.Comunication.Info.EvaluationEvent;
import Evaluation_AssignmentService.Comunication.Publisher.Publisher;
import Evaluation_AssignmentService.ProcessEvaluation.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessRepository.FormatARepository;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class that manages logic and validations for @FormatA processes.
 */
@Service
public class FormatAService extends ProcessService<FormatA> {
    private final FormatARepository formatARepository;
    private final byte maxAttempts = 3;

    @Autowired
    public FormatAService(FormatARepository repository, ProcessFactory processFactory, Publisher pPublisher) {
        super(repository, processFactory, pPublisher);
        this.formatARepository = repository;
    }

    @Override
    protected void validateRequirements(FormatA pCurrentProcess) {
        if(pCurrentProcess.getAttempts() >= maxAttempts) {
            pCurrentProcess.setStatus(EnumProcessStatus.FAILED);
        }
    }

    /**
     * Validates that a previous Draft process exists and has been approved
     * before allowing the creation of a new FormatA.
     *
     * @param pNewProcess the new FormatA process to validate
     */
    @Override
    protected void validateBeforeCreate(FormatA pNewProcess) {
        if(pNewProcess == null)
            throw new ProcessException(EnumTypeExceptions.NOT_FOUND);
    }


    @Override
    protected void sendStatusChangeEvent(FormatA pProcess) {
        EnumDegreeWorkStateType vNewStatus;
        switch (pProcess.getStatus()){
            case PENDING -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_SUBMITTED;
            case APPROVED -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_APPROVED;
            case REJECTED -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_REJECTED;
            case FAILED -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A_FAILED;
            default -> vNewStatus = EnumDegreeWorkStateType.FORMAT_A;//Nunca se cumple
        }
        publisher.sendToModifierQueue(new EvaluationEvent(pProcess.getDegreeworkId(), vNewStatus));
    }
}

