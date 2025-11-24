package microservice.ProcessEvaluation.Dtos.Input;

public class AssignmentDTO extends CoreProcessDTO{
    private Long evaluatorId;

    public AssignmentDTO() {
        super();
    }
    public Long getEvaluatorId() {return evaluatorId;}
    public void setEvaluatorId(Long evaluatorId) {this.evaluatorId = evaluatorId;}
}
