package microservice.ProcessEvaluation.Services;

import microservice.ProcessEvaluation.Repositories.DegreeWorkRepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.DegreeWorkIds;
import microservice.SecurityComponent.ProcessException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing DegreeWork identifiers.
 * Provides validation, creation, and initialization operations for DegreeWorkIds.
 */
@Service
public class DegreeWorkService {

    private final DegreeWorkRepository degreeWorkRepository;

    /**
     * Creates a new instance of DegreeWorkService using the provided repository.
     * Automatically initializes a set of predefined DegreeWorkIds.
     *
     * @param existingRepo repository used for persistence operations
     */
    public DegreeWorkService(DegreeWorkRepository existingRepo) {
        this.degreeWorkRepository = existingRepo;
        initializr();
    }

    /**
     * Stores a new DegreeWork identifier.
     * Throws an exception if the identifier already exists.
     *
     * @param pDegreeWorkId identifier to store
     */
    public void saveDegreeWorkId(Long pDegreeWorkId) {
        if (degreeWorkRepository.existsById(pDegreeWorkId))
            throw new ProcessException(EnumTypeExceptions.DEGREEWORKID_EXISTING);
        else degreeWorkRepository.save(new DegreeWorkIds(pDegreeWorkId));
    }

    /**
     * Validates that a DegreeWork identifier exists in the repository.
     * Throws an exception if it does not.
     *
     * @param pDegreeWorkId identifier to validate
     */
    public void validateExistingId(Long pDegreeWorkId) {
        if(!existsId(pDegreeWorkId))
            throw new ProcessException(EnumTypeExceptions.DEGREEWORKID_NOT_FOUND);
    }

    /**
     * Checks whether a DegreeWork identifier exists.
     *
     * @param pNameDegreeWorkId identifier to check
     * @return true if exists, false otherwise
     */
    public boolean existsId(Long pNameDegreeWorkId) {
        return degreeWorkRepository.existsById(pNameDegreeWorkId);
    }

    /**
     * Initializes the repository with predefined DegreeWork identifiers.
     */
    private void initializr(){
        degreeWorkRepository.save(new DegreeWorkIds(1L));
        degreeWorkRepository.save(new DegreeWorkIds(2L));
        degreeWorkRepository.save(new DegreeWorkIds(3L));
    }
}

