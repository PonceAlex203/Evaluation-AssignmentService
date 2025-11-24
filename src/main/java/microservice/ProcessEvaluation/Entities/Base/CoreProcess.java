package microservice.ProcessEvaluation.Entities.Base;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Embeddable
public class CoreProcess {
    private Long degreeWorkId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    protected CoreProcess() { date = new Date(); }

    public CoreProcess(Long degreeWorkId) {
        this.degreeWorkId = degreeWorkId;
        date = new Date();
    }

    public Long getDegreeWorkId() { return degreeWorkId;}
    public void setDegreeWorkId(Long degreeWorkId) { this.degreeWorkId = degreeWorkId;}
    public Date getDate() { return date;}
    public void setDate(Date date) { this.date = date;}
}
