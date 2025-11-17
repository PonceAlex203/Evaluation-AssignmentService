package Evaluation_AssignmentService.ProcessService;

import Evaluation_AssignmentService.Dto.FormatADTO;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.ProcessRepository.FormatARepository;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class that manages logic and validations for @FormatA processes.
 */
@Service
public class FormatAService extends ProcessService<FormatA, FormatADTO> {
    private final FormatARepository formatARepository;
    private final byte maxAttempts = 3;

    @Autowired
    public FormatAService(FormatARepository repository, ProcessFactory processFactory) {
        super(repository, processFactory);
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
    protected void updateInternalData(FormatA pUpdateProcess) {
        if(pUpdateProcess.getStatus().equals(EnumProcessStatus.REJECTED))
            pUpdateProcess.increaseAttempts();
    }

    /**
     * Synchronizes additional data specific to FormatA entities.
     *
     * @param pCurrentProcess the current FormatA instance in the database
     * @param pUpdateProcess the new FormatA data to synchronize
     */
    @Override
    protected void SynchronizeData(FormatA pCurrentProcess, FormatA pUpdateProcess){
        super.SynchronizeData(pCurrentProcess, pUpdateProcess);
        pCurrentProcess.setCompanyLetterPath(pCurrentProcess.getCompanyLetterPath());
    }
}

