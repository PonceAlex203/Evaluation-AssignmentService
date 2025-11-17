package Evaluation_AssignmentService.ProcessService;

import Evaluation_AssignmentService.ProcessRepository.DegreeWorkRepository;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.DegreeWorkIds;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.springframework.stereotype.Service;

@Service
public class DegreeWorkService {

    private final DegreeWorkRepository degreeWorkRepository;

    public DegreeWorkService(DegreeWorkRepository existingRepo) {
        this.degreeWorkRepository = existingRepo;
        initializr();
    }

    public void saveDegreeWorkId(Long pDegreeWorkId) {
        if (degreeWorkRepository.existsById(pDegreeWorkId))
            throw new ProcessException(EnumTypeExceptions.DEGREEWORKID_EXISTING);
        degreeWorkRepository.save(new DegreeWorkIds(pDegreeWorkId));
    }

    public void validateExistingId(Long pDegreeWorkId) {
        if(!existsId(pDegreeWorkId))
            throw new ProcessException(EnumTypeExceptions.DEGREEWORKID_NOT_FOUND);
    }
    public boolean existsId(Long pNameDegreeWorkId) {
        return degreeWorkRepository.existsById(pNameDegreeWorkId);
    }

    private void initializr(){
        degreeWorkRepository.save(new DegreeWorkIds(1L));
        degreeWorkRepository.save(new DegreeWorkIds(2L));
        degreeWorkRepository.save(new DegreeWorkIds(3L));
    }
}
