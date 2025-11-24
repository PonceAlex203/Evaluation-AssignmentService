package microservice.ProcessEvaluation.Dtos.Input;

/**
 * DTO for evaluating a process.
 * Contains comment and new status to update.
 */
public class EvaluationDTO extends CoreProcessDTO{
    private Long evaluatorId;
    private String comment;
    public EvaluationDTO() { }

    public Long getEvaluatorId() {return evaluatorId;}
    public void setEvaluatorId(Long evaluatorId) {this.evaluatorId = evaluatorId;}
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}

