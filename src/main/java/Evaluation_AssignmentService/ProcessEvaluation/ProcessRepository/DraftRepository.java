package Evaluation_AssignmentService.ProcessEvaluation.ProcessRepository;

import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.Draft;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Draft} entities.
 * Extends {@link ProcessRepository} to provide specific operations for Draft processes.
 */
@Repository
public interface DraftRepository extends ProcessRepository<Draft> {
}

