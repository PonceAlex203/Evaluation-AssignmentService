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
    Optional<Draft> findEvaluator(
            Long pDegreeWorkId,
            Long pEvaluatorId,
            EnumProcessStatus pEvaluationStatus);

    // 3. Listar drafts por evaluador + estado (en cualquiera de las dos evaluaciones)
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
    WHERE d.departmentHeadId = :pDepartmentHeadId
      AND d.core.degreeWorkId = :pDegreeWorkId
      AND (d.evaluation IS NULL OR d.evaluation.evaluatorId IS NULL)
      AND (d.evaluation2 IS NULL OR d.evaluation2.evaluatorId IS NULL)
""")
    Optional<Draft> findUnassignedDraft(
            Long pDepartmentHeadId,
            Long pDegreeWorkId);

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
    WHERE d.departmentHeadId = :pDepartmentHeadId
      AND (
            d.evaluation IS NULL 
            OR d.evaluation.evaluatorId IS NULL
            OR d.evaluation2 IS NULL
            OR d.evaluation2.evaluatorId IS NULL
          )
""")
    List<Draft> findPendingAssignmentBy(Long pDepartmentHeadId);


}
