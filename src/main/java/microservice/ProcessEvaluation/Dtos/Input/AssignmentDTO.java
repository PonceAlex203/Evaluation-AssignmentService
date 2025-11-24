package microservice.ProcessEvaluation.Dtos.Input;

/**
 * Data transfer object for single evaluator assignment
 */
public class AssignmentDTO extends CoreProcessDTO{
    private Long evaluatorId;

    public AssignmentDTO() {
        super();
    }
    public Long getEvaluatorId() {return evaluatorId;}
    private void setEvaluatorId(Long evaluatorId) {this.evaluatorId = evaluatorId;}
}
