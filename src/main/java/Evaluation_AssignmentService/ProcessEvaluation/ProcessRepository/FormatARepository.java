package Evaluation_AssignmentService.ProcessEvaluation.ProcessRepository;

import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.FormatA;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link FormatA} entities.
 * Extends {@link ProcessRepository} to provide specific operations for FormatA processes.
 */
@Repository
public interface FormatARepository extends ProcessRepository<FormatA> {
}
