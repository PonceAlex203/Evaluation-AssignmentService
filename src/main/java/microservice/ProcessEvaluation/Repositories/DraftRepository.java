package microservice.ProcessEvaluation.Repositories;

import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Draft} entities.
 * Extends {@link ProcessRepository} to provide specific operations for Draft processes.
 */
@Repository
public interface DraftRepository extends ProcessRepository<Draft, DraftRepository> {


    @Query("""
    SELECT d FROM Draft d
    WHERE d.core.degreeWorkId = :pDegreeWorkId
      AND d.evaluation IS NOT NULL
      AND d.evaluation2 IS NOT NULL
      AND (
            (d.evaluation.evaluatorId = :pEvaluatorId
             AND d.evaluation.evaluationStatus = :pEvaluationStatus)
         OR (d.evaluation2.evaluatorId = :pEvaluatorId
             AND d.evaluation2.evaluationStatus = :pEvaluationStatus)
          )
    """)
    Optional<Draft> findPendingEvaluate(
            Long pDegreeWorkId,
            Long pEvaluatorId,
            EnumProcessStatus pEvaluationStatus);

    @Query("""
        SELECT d FROM Draft d
        WHERE 
               (d.evaluation.evaluatorId = :pEvaluatorId
                AND d.evaluation.evaluationStatus = :pEvaluationStatus)
            OR (d.evaluation2.evaluatorId = :pEvaluatorId
                AND d.evaluation2.evaluationStatus = :pEvaluationStatus)
    """)
    List<Draft> findAllBy(
            Long pEvaluatorId,
            EnumProcessStatus pEvaluationStatus);


    @Query("""
    SELECT d FROM Draft d
    WHERE d.core.degreeWorkId = :pDegreeWorkId
      AND (d.evaluation IS NULL OR d.evaluation.evaluatorId IS NULL)
      AND (d.evaluation2 IS NULL OR d.evaluation2.evaluatorId IS NULL)
    """)
    Optional<Draft> findUnassignedDraft(Long pDegreeWorkId);

    @Query("""
    SELECT d FROM Draft d
    WHERE d.core.degreeWorkId = :pDegreeWorkId
      AND d.generalStatus IN :pStatuses
""")
    Optional<Draft> findAllBy(
            Long pDegreeWorkId,
            List<EnumDegreeWorkStateType> pStatuses
    );

    @Query("""
    SELECT d FROM Draft d
    WHERE d.evaluation IS NULL 
       OR d.evaluation2 IS NULL
    """)
    List<Draft> findPendingAssignment();

    @Override
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.evaluation.evaluationStatus = :pEvaluationStatus
        OR p.evaluation2.evaluationStatus = :pEvaluationStatus
    """)
    List<Draft> findByEvaluationStatus(EnumProcessStatus pEvaluationStatus);

}
