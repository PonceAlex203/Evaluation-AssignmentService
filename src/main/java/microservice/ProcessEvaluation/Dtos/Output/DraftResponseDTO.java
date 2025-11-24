package microservice.ProcessEvaluation.Dtos.Output;

import java.util.Date;

/**
 * Response DTO for draft processes
 */
public class DraftResponseDTO extends ProcessResponseDTO{
    private Date deadline;

    /**
     * @return the draft deadline
     */
    public Date getDeadline() {return deadline;}
    public void setDeadline(Date deadline) {this.deadline = deadline;}
}