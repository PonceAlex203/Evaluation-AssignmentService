package microservice.ProcessEvaluation.Dtos.Input;

/**
 * Data transfer object for process evaluation
 */
public class EvaluationDTO extends CoreProcessDTO{
    private String comment;
    public EvaluationDTO() { }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}

