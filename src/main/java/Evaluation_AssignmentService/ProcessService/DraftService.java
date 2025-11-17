package Evaluation_AssignmentService.ProcessService;

import Evaluation_AssignmentService.Dto.DraftDTO;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.ProcessRepository.DraftRepository;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class that manages logic and validations for @Draft processes.
 */
@Service
public class DraftService extends ProcessService<Draft, DraftDTO>{

    @Autowired
    private final DraftRepository draftRepository;
    private final FormatAService formatAService;

    public DraftService(DraftRepository repository, ProcessFactory processFactory, FormatAService formatAService) {
        super(repository, processFactory);
        this.draftRepository = repository;
        this.formatAService = formatAService;
    }

    /**
     * Validates that the corresponding FormatA process exists and is approved
     * before allowing the creation of a new Draft.
     */
    @Override
    protected void validateRequirements(Draft pCurrentProcess) {
        if(pCurrentProcess.getDaysPassed() >= 60)
            throw new ProcessException(EnumTypeExceptions.EXPIRED_TIME);
    }
    /**
     * Updates internal Draft data such as days passed.
     *
     * @param pUpdateProcess the Draft entity to update
     */
    @Override
    protected void updateInternalData(Draft pUpdateProcess) {
        pUpdateProcess.updateDaysPassed();
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
}

