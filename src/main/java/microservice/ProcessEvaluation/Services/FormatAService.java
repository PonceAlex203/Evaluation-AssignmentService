package microservice.ProcessEvaluation.Services;

import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
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
    public FormatA getApproved(Long pDegreeWorkId) {
        return repository.findByDegreeWorkAndGeneralStatus(pDegreeWorkId,EnumDegreeWorkStateType.DRAFT).orElse(null);
    }

    @Override
    protected void validateRequirements(FormatA pCurrentProcess) {
        if(pCurrentProcess.getAttempts() >= maxAttempts) {
            pCurrentProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_FAILED);
        }
    }

    @Override
    protected void executeEvaluation(FormatA pProcess, Long pIdEvaluator, EnumProcessStatus pNewStatus, String pComment) {
        pProcess.getEvaluation().evaluate(pNewStatus,pComment);
        if(pNewStatus == EnumProcessStatus.APPROVED)
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT);
        else
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_FAILED);
    }

    @Override
    protected void executeAssignment(FormatA pProcess, Long pIdEvaluator) {
        pProcess.setEvaluator(pIdEvaluator);
    }

    @Override
    protected void executeUpload(FormatA vCurrentProcess) {
        vCurrentProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED);
    }

    @Override
    protected void validateBeforeCreate(FormatA pNewProcess) {
        if(pNewProcess == null)
            throw new ProcessException(EnumTypeExceptions.NOT_FOUND);
    }

}
