package microservice.ProcessEvaluation.Dtos.Output;

import java.util.Date;

public class DraftResponseDTO extends ProcessResponseDTO{
    private Date deadline;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
