package Evaluation_AssignmentService.ProcessRepository;

import Evaluation_AssignmentService.SecurityComponent.DegreeWorkIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreeWorkRepository extends JpaRepository<DegreeWorkIds, Long> {
}
