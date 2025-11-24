package microservice.ProcessEvaluation.Dtos.Output;

import java.util.Date;

public abstract class CoreProcessResponseDTO {
    protected Long degreeWorkId;
    protected Date date;

    public CoreProcessResponseDTO() {}

    public Long getDegreeWorkId() { return degreeWorkId;}
    public void setDegreeWorkId(Long degreeWorkId) { this.degreeWorkId = degreeWorkId;}
    public Date getDate() { return date;}
    public void setDate(Date date) { this.date = date;}
}
