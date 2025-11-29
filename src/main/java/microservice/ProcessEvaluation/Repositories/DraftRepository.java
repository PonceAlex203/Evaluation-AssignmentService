package microservice.ProcessEvaluation.Repositories;

import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Enums.EnumAssignmentStatus;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Draft} entities.
 * Extends {@link ProcessRepository} to provide specific operations for Draft processes.
 */
@Repository
public interface DraftRepository extends ProcessRepository<Draft, DraftRepository>  {

    @Query("""
        SELECT d FROM Draft d
        WHERE d.evaluation.evaluatorId = :pEvaluatorId
           OR d.evaluation2.evaluatorId = :pEvaluatorId
        """)
    List<Draft> findByAnyEvaluatorId(Long pEvaluatorId);

    @Override
    @Query("""
        SELECT d FROM Draft d
        WHERE 
            (d.evaluation.evaluatorId = :pEvaluatorId
             AND d.evaluation.evaluationStatus = 'PENDING')
         OR 
            (d.evaluation2.evaluatorId = :pEvaluatorId
             AND d.evaluation2.evaluationStatus = 'PENDING')
        """)
    List<Draft> findPendingEvaluationsByEvaluator(Long pEvaluatorId);

    @Query("SELECT p FROM #{#entityName} p " +
            "WHERE p.assignmentStatus IN :pStatusList " +
            "AND p.departmentHeadId = :pDepartmentHeadId")
    List<Draft> findByStatusInAndDept(@Param("pStatusList") List<EnumAssignmentStatus> pStatusList,
                                  @Param("pDepartmentHeadId") Long pDepartmentHeadId);

}

