package Evaluation_AssignmentService.ProcessRepository;

import Evaluation_AssignmentService.ProcessEntity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Presentation} entities.
 * Extends {@link JpaRepository} to provide CRUD operations on Presentation data.
 */
@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {
}
