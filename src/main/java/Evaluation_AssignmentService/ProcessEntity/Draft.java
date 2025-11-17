package Evaluation_AssignmentService.ProcessEntity;

import Evaluation_AssignmentService.Enum.EnumTypeProcess;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Entity representing a Draft process.
 * Tracks submission time and calculates days since creation.
 */
@Entity
@Table(
        name = "draft",
        uniqueConstraints = @UniqueConstraint(columnNames = "degreework_id")
)
public class Draft extends BaseProcess {

    /** Number of days passed since the draft creation. */
    private long daysPassed = 0;
    private Date deadline;//esto mejor que contar dias

    /** Default constructor. */
    public Draft() { super(); }

    /**
     * Constructor with degree work ID.
     * @param degreeworkId degree work ID
     */
    public Draft(Long degreeworkId) {
        super(degreeworkId);
    }

    /**
     * Constructor with degree work ID and URL.
     * @param pDegreeWork degree work ID
     * @param pUrl draft document URL
     */
    public Draft(Long pDegreeWork, String pUrl) {
        this.url = pUrl;
        this.degreeworkId = pDegreeWork;
    }

    /**
     * Calculates the number of days passed since the draft creation.
     * @return days passed as long
     */
    private long calculateDaysPassed() {
        Date vCurrentDate = new Date();
        long vDiffMillis = vCurrentDate.getTime() - this.date.getTime();
        return TimeUnit.DAYS.convert(vDiffMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Updates the daysPassed attribute with the latest calculation.
     */
    public void updateDaysPassed(){ calculateDaysPassed(); }

    /**
     * Gets the number of days passed since creation.
     * @return days passed
     */
    public long getDaysPassed() {
        daysPassed = calculateDaysPassed();
        return daysPassed;
    }

    /**
     * Sets the days passed value (recalculated internally).
     * @param daysPassed ignored external value
     */
    public void setDaysPassed(long daysPassed) {
        calculateDaysPassed();
    }

    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.DRAFT;
    }

}
