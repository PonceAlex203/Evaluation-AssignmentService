package microservice.ProcessEvaluation.Repositories;

import microservice.SecurityComponent.DegreeWorkIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreeWorkRepository extends JpaRepository<DegreeWorkIds, Long> {
}
