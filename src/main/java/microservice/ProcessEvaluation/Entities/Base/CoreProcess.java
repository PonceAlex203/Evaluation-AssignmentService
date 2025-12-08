package microservice.ProcessEvaluation.Entities.Base;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

/**
 * Represents core metadata for a process, including its associated degree work
 * and the date on which the process is created.
 */
@Embeddable
public class CoreProcess {

    private Long degreeWorkId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    /**
     * Protected constructor used by JPA.
     * Initializes the process date with the current timestamp.
     */
    protected CoreProcess() {
        date = new Date();
    }

    /**
     * Creates a new core process.
     *
     * @param degreeWorkId the identifier of the degree work
     */
    public CoreProcess(Long degreeWorkId) {
        this.degreeWorkId = degreeWorkId;
        date = new Date();
    }

    /**
     * @return the degree work identifier
     */
    public Long getDegreeWorkId() { return degreeWorkId; }

    /**
     * Sets the degree work identifier.
     *
     * @param degreeWorkId the new degree work identifier
     */
    private void setDegreeWorkId(Long degreeWorkId) { this.degreeWorkId = degreeWorkId; }

    /**
     * @return the creation date of the process
     */
    public Date getDate() { return date; }

    /**
     * Sets the process date.
     *
     * @param date the new process date
     */
    private void setDate(Date date) { this.date = date; }
}

