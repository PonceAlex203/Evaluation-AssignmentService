package microservice.ProcessEvaluation.Repositories;

import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Draft entities.
 * Extends the generic ProcessRepository and adds draft-specific queries.
 */
@Repository
public interface DraftRepository extends ProcessRepository<Draft, DraftRepository> {

    /**
     * Finds a draft for a given degree work and evaluator where the evaluation
     * matches the provided status in either primary or secondary evaluation.
     *
     * @param pDegreeWorkId the degree work identifier
     * @param pEvaluatorId the evaluator identifier
     * @param pEvaluationStatus the evaluation status to match
     * @return an optional containing the draft if found
     */
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

    /**
     * Retrieves all drafts evaluated by a specific evaluator and matching a given status.
     *
     * @param pEvaluatorId the evaluator identifier
     * @param pEvaluationStatus the evaluation status to filter
     * @return a list of drafts
     */
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

    /**
     * Finds an unassigned draft for the given degree work, where both evaluations are null.
     *
     * @param pDegreeWorkId the degree work identifier
     * @return an optional draft
     */
    @Query("""
        SELECT d FROM Draft d
        WHERE d.core.degreeWorkId = :pDegreeWorkId
          AND (d.evaluation IS NULL OR d.evaluation.evaluatorId IS NULL)
          AND (d.evaluation2 IS NULL OR d.evaluation2.evaluatorId IS NULL)
    """)
    Optional<Draft> findUnassignedDraft(Long pDegreeWorkId);

    /**
     * Finds drafts by degree work and status list.
     *
     * @param pDegreeWorkId the degree work identifier
     * @param pStatuses the list of general statuses to filter
     * @return an optional containing the draft if found
     */
    @Query("""
        SELECT d FROM Draft d
        WHERE d.core.degreeWorkId = :pDegreeWorkId
          AND d.generalStatus IN :pStatuses
    """)
    Optional<Draft> findAllBy(
            Long pDegreeWorkId,
            List<EnumDegreeWorkStateType> pStatuses
    );

    /**
     * Retrieves all drafts that have at least one missing evaluation assignment.
     *
     * @return a list of drafts
     */
    @Query("""
        SELECT d FROM Draft d
        WHERE d.evaluation IS NULL 
           OR d.evaluation2 IS NULL
    """)
    List<Draft> findPendingAssignment();

    /**
     * Finds drafts by evaluation status in either primary or secondary evaluation.
     *
     * @param pEvaluationStatus the evaluation status
     * @return a list of drafts
     */
    @Override
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.evaluation.evaluationStatus = :pEvaluationStatus
        OR p.evaluation2.evaluationStatus = :pEvaluationStatus
    """)
    List<Draft> findByEvaluationStatus(EnumProcessStatus pEvaluationStatus);
}
