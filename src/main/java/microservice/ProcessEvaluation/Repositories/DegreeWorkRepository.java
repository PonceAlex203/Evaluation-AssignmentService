package microservice.ProcessEvaluation.Repositories;

import microservice.SecurityComponent.DegreeWorkIds;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing DegreeWorkIds entities.
 * Provides basic CRUD operations inherited from JpaRepository.
 */
public interface DegreeWorkRepository extends JpaRepository<DegreeWorkIds, Long> {
}
