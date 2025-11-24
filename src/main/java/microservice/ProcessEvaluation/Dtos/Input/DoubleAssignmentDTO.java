package microservice.ProcessEvaluation.Dtos.Input;

public class DoubleAssignmentDTO extends CoreProcessDTO{
    private Long evaluatorId1;
    private Long evaluatorId2;

    public Long getEvaluatorId1() {return evaluatorId1;}
    public void setEvaluatorId1(Long evaluatorId1) {this.evaluatorId1 = evaluatorId1;}
    public Long getEvaluatorId2() {return evaluatorId2;}
    public void setEvaluatorId2(Long evaluatorId2) {this.evaluatorId2 = evaluatorId2;}
}
