package Evaluation_AssignmentService.ProcessRepository;

import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.BaseProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for managing {@link BaseProcess} entities.
 * Provides basic database operations and custom query methods for process management.
 *
 * @param <T> the type of process that extends {@link BaseProcess}
 */
@Repository
public interface ProcessRepository<T extends BaseProcess> extends JpaRepository<T, Long> {

    /**
     * Retrieves a list of processes filtered by their current status.
     *
     * @param status the process status to filter by
     * @return a list of processes with the given status
     */
    List<T> findByStatus(EnumProcessStatus status);

    /**
     * Finds a process by its associated degree work ID.
     *
     * @param pDegreeWorkId the degree work ID
     * @return an {@link Optional} containing the process if found, or empty otherwise
     */
    Optional<T> findByDegreeworkId(Long pDegreeWorkId);
}
