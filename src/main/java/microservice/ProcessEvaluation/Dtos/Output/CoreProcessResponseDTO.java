package microservice.ProcessEvaluation.Dtos.Output;

import java.util.Date;

/**
 * Base response DTO containing common process attributes
 */
public abstract class CoreProcessResponseDTO {
    protected Long degreeWorkId;
    protected Date date;

    public CoreProcessResponseDTO() {}

    /**
     * @return the degree work identifier
     */
    public Long getDegreeWorkId() { return degreeWorkId;}
    public void setDegreeWorkId(Long degreeWorkId) { this.degreeWorkId = degreeWorkId;}

    /**
     * @return the process date
     */
    public Date getDate() { return date;}
    public void setDate(Date date) { this.date = date;}
}
