package microservice.ProcessEvaluation.Repositories;

import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for managing {@link BaseProcess} entities.
 * Provides basic database operations and custom query methods for process management.
 *
 * @param <T> the type of process that extends {@link BaseProcess}
 */
@Repository
public interface ProcessRepository<T extends BaseProcess, R extends ProcessRepository<T, R>>
        extends JpaRepository<T, Long> {

    // 1. Buscar proceso por DegreeWorkId
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
    """)
    Optional<T> findBy(Long pDegreeWorkId);


    // 2. Buscar procesos por evaluador + estado de evaluacion
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.evaluation.evaluatorId = :pEvaluatorId
          AND p.evaluation.evaluationStatus = :pEvaluationStatus
    """)
    List<T> findByEvaluatorAndEvaluationStatus(Long pEvaluatorId, EnumProcessStatus pEvaluationStatus);

    // 4. Buscar por degreeWorkId + estado general
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
          AND p.generalStatus = :pGeneralStatus
    """)
    Optional<T> findByDegreeWorkAndGeneralStatus(Long pDegreeWorkId, EnumDegreeWorkStateType pGeneralStatus);

    @Query(""" 
    SELECT p FROM #{#entityName} p
     WHERE p.core.degreeWorkId = :pDegreeWorkId 
     AND p.evaluation.evaluatorId = :pEvaluatorId 
     AND p.evaluation.evaluationStatus = :pEvaluationStatus 
     """)
    Optional<T> findByDegreeWorkEvaluatorAndEvaluationStatus( Long pDegreeWorkId, Long pEvaluatorId, EnumProcessStatus pEvaluationStatus);

    // 5. Buscar por degreeWorkId + estado interno de evaluación
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
          AND p.evaluation.evaluationStatus = :pEvaluationStatus
    """)
    Optional<T> findByDegreeWorkIdAndEvaluationStatus(Long pDegreeWorkId, EnumProcessStatus pEvaluationStatus);

    // 7. Procesos donde NO hay evaluacion (evaluation null)
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
          AND p.evaluation IS NULL
    """)
    Optional<T> findUnassignedBy(Long pDegreeWorkId);

}

