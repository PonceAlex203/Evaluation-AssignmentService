package microservice.ProcessEvaluation.Dtos.Input;

/**
 * Data transfer object for multiple evaluator assignments
 */
public class DoubleAssignmentDTO extends CoreProcessDTO{
    private Long departmentHeadId;
    private Long evaluatorId1;
    private Long evaluatorId2;

    public Long getEvaluatorId1() {return evaluatorId1;}
    public Long getEvaluatorId2() {return evaluatorId2;}

    public Long getDepartmentHeadId() {return departmentHeadId;}
    public void setDepartmentHeadId(Long departmentHeadId) {this.departmentHeadId = departmentHeadId;}

    public void setEvaluatorId1(Long evaluatorId1) {this.evaluatorId1 = evaluatorId1;}
    public void setEvaluatorId2(Long evaluatorId2) {this.evaluatorId2 = evaluatorId2;}
}