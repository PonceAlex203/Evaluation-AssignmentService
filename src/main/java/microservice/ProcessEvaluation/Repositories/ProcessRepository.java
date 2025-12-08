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
 * Generic repository for managing process entities.
 * Provides reusable queries for all process types.
 *
 * @param <T> the process type
 * @param <R> the concrete repository type
 */
@Repository
public interface ProcessRepository<T extends BaseProcess, R extends ProcessRepository<T, R>>
        extends JpaRepository<T, Long> {

    /**
     * Retrieves a process by its degree work identifier.
     *
     * @param pDegreeWorkId the degree work identifier
     * @return an optional containing the process if found
     */
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
    """)
    Optional<T> findBy(Long pDegreeWorkId);

    /**
     * Retrieves processes filtered by evaluation status.
     *
     * @param pEvaluationStatus the evaluation status
     * @return a list of matching processes
     */
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.evaluation.evaluationStatus = :pEvaluationStatus
    """)
    List<T> findByEvaluationStatus(EnumProcessStatus pEvaluationStatus);

    /**
     * Retrieves a process filtered by degree work identifier and general status.
     *
     * @param pDegreeWorkId the degree work identifier
     * @param pGeneralStatus the general process status
     * @return an optional containing the process
     */
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
          AND p.generalStatus = :pGeneralStatus
    """)
    Optional<T> findByDegreeWorkAndGeneralStatus(Long pDegreeWorkId, EnumDegreeWorkStateType pGeneralStatus);

    /**
     * Retrieves a process filtered by degree work identifier and internal evaluation status.
     *
     * @param pDegreeWorkId the degree work identifier
     * @param pEvaluationStatus the evaluation status
     * @return an optional containing the process
     */
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
          AND p.evaluation.evaluationStatus = :pEvaluationStatus
    """)
    Optional<T> findByDegreeWorkIdAndEvaluationStatus(Long pDegreeWorkId, EnumProcessStatus pEvaluationStatus);

    /**
     * Finds processes that have no evaluator assigned.
     *
     * @param pDegreeWorkId the degree work identifier
     * @return an optional process
     */
    @Query("""
        SELECT p FROM #{#entityName} p
        WHERE p.core.degreeWorkId = :pDegreeWorkId
          AND p.evaluation IS NULL
    """)
    Optional<T> findUnassignedBy(Long pDegreeWorkId);
}


