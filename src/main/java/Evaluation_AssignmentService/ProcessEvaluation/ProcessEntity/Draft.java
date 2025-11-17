package Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity;

import Evaluation_AssignmentService.ProcessEvaluation.Enum.EnumTypeProcess;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Calendar;
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
    private Date deadline;

    /** Default constructor. */
    public Draft() {
        super();
        initializeDeadline();
    }

    /**
     * Constructor with degree work ID.
     * @param degreeworkId degree work ID
     */
    public Draft(Long degreeworkId) {
        super(degreeworkId);
        initializeDeadline();
    }

    /**
     * Constructor with degree work ID and URL.
     * @param pDegreeWork degree work ID
     * @param pUrl draft document URL
     */
    public Draft(Long pDegreeWork, String pUrl) {
        this.url = pUrl;
        this.degreeworkId = pDegreeWork;
        initializeDeadline();
    }

    private void initializeDeadline() {
        Calendar vCal = Calendar.getInstance();
        vCal.setTime(this.date);
        vCal.add(Calendar.DAY_OF_YEAR, 90);
        this.deadline = vCal.getTime();
    }

    public boolean isExpired() {
        return new Date().after(deadline);
    }

    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.DRAFT;
    }

}
