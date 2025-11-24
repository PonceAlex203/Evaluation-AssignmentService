package microservice.ProcessEvaluation.Dtos.Input;

/**
 * Data transfer object for process evaluation
 */
public class EvaluationDTO extends CoreProcessDTO{
    private Long evaluatorId;
    private String comment;
    public EvaluationDTO() { }

    public Long getEvaluatorId() {return evaluatorId;}
    public String getComment() {
        return comment;
    }

    public void setEvaluatorId(Long evaluatorId) {this.evaluatorId = evaluatorId;}
    public void setComment(String comment) {
        this.comment = comment;
    }
}

