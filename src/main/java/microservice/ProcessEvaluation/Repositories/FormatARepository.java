package microservice.ProcessEvaluation.Repositories;

import microservice.ProcessEvaluation.Entities.Process.FormatA;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link FormatA} entities.
 * Extends {@link ProcessRepository} to provide specific operations for FormatA processes.
 */
@Repository
public interface FormatARepository extends ProcessRepository<FormatA> {
}
