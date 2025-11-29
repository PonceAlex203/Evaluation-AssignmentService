package microservice.ProcessEvaluation.Repositories;

import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Draft} entities.
 * Extends {@link ProcessRepository} to provide specific operations for Draft processes.
 */
@Repository
public interface DraftRepository extends ProcessRepository<Draft, DraftRepository> {

    // 1. Buscar draft por degreeWorkId + evaluadorId (en cualquiera de las 2 evaluaciones)
    @Query("""
        SELECT d FROM Draft d
        WHERE d.core.degreeWorkId = :pDegreeWorkId
          AND (
               (d.evaluation IS NOT NULL
                AND d.evaluation.evaluatorId = :pEvaluatorId)
            OR (d.evaluation2 IS NOT NULL
                AND d.evaluation2.evaluatorId = :pEvaluatorId)
          )
    """)
    Optional<Draft> findByDegreeWorkIdAndAnyEvaluator(Long pDegreeWorkId, Long pEvaluatorId);

    // 2. Verificar si un evaluador ya evaluó (por ID y estado) evitar evaluaciones duplicadas.
    @Query("""
        SELECT d FROM Draft d
        WHERE d.core.degreeWorkId = :pDegreeWorkId
          AND (
               (d.evaluation.evaluatorId = :pEvaluatorId
                AND d.evaluation.evaluationStatus = :pEvaluationStatus)
            OR (d.evaluation2.evaluatorId = :pEvaluatorId
                AND d.evaluation2.evaluationStatus = :pEvaluationStatus)
          )
    """)
    Optional<Draft> findEvaluatorEvaluationStatus(
            Long pDegreeWorkId,
            Long pEvaluatorId,
            EnumProcessStatus pEvaluationStatus);


    // 3. Listar drafts por evaluador + estado (en cualquiera de las dos evaluaciones) para saber todo lo pendiente que tiene un evaluador.
    @Query("""
        SELECT d FROM Draft d
        WHERE 
               (d.evaluation.evaluatorId = :pEvaluatorId
                AND d.evaluation.evaluationStatus = :pEvaluationStatus)
            OR (d.evaluation2.evaluatorId = :pEvaluatorId
                AND d.evaluation2.evaluationStatus = :pEvaluationStatus)
    """)
    List<Draft> findAllByEvaluatorAndStatus(
            Long pEvaluatorId,
            EnumProcessStatus pEvaluationStatus);


    @Query("""
    SELECT d FROM Draft d
    WHERE d.departmentHeadId = :pDepartmentHeadId
      AND d.core.degreeWorkId = :pDegreeWorkId
      AND (d.evaluation IS NULL OR d.evaluation.evaluatorId IS NULL)
      AND (d.evaluation2 IS NULL OR d.evaluation2.evaluatorId IS NULL)
""")
    Optional<Draft> findUnassignedDraftByDepartmentHeadAndDegreeWorkId(
            Long pDepartmentHeadId,
            Long pDegreeWorkId);


    // 5. Traer Drafts donde uno de los evaluadores ya evaluó, pero el otro no(n flujo de Drafts)
    @Query("""
        SELECT d FROM Draft d
        WHERE 
            (
                d.evaluation.evaluationStatus = :pCompletedStatus
                AND (d.evaluation2.evaluationStatus IS NULL 
                     OR d.evaluation2.evaluationStatus = :pPendingStatus)
            )
         OR
            (
                d.evaluation2.evaluationStatus = :pCompletedStatus
                AND (d.evaluation.evaluationStatus IS NULL 
                     OR d.evaluation.evaluationStatus = :pPendingStatus)
            )
    """)
    List<Draft> findHalfEvaluatedDrafts(
            EnumProcessStatus pCompletedStatus,
            EnumProcessStatus pPendingStatus);

}
