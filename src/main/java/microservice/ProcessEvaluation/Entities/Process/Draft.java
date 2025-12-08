package microservice.ProcessEvaluation.Entities.Process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Embedded;
import jakarta.persistence.PrePersist;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents the draft process of a degree work.
 * Includes a deadline and a secondary evaluation.
 */
@Entity
@Table(
        name = "draft",
        uniqueConstraints = @UniqueConstraint(columnNames = "degreework_id")
)
public class Draft extends BaseProcess {

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    @Embedded
    private Evaluation evaluation2;

    /**
     * Default constructor used by JPA.
     * Initializes the deadline and assigns default state.
     */
    public Draft() {
        super();
        generalStatus = EnumDegreeWorkStateType.DRAFT_SUBMITTED;
    }

    /**
     * Creates a draft process with core information and a URL.
     *
     * @param pCore the core process information
     * @param pUrl the associated file URL
     */
    public Draft(CoreProcess pCore, String pUrl) {
        super(pCore, pUrl);
        generalStatus = EnumDegreeWorkStateType.DRAFT_SUBMITTED;
    }

    /**
     * Initializes the deadline only once before persisting.
     */
    @PrePersist
    private void initializeDeadline() {
        if(deadline != null || this.getCore() == null) return;

        Calendar vCal = Calendar.getInstance();
        vCal.setTime(this.getCore().getDate());
        vCal.add(Calendar.DAY_OF_YEAR, 90);
        this.deadline = vCal.getTime();
    }
    /**
     * @return the secondary evaluation
     */
    public Evaluation getEvaluation2() { return evaluation2; }

    /**
     * @return the submission deadline
     */
    public Date getDeadline() {return deadline;}

    private void setDeadline(Date deadline) { this.deadline = deadline; }

    /**
     * Sets the secondary evaluation.
     *
     * @param evaluation2 the evaluation object
     */
    public void setEvaluation2(Evaluation evaluation2) { this.evaluation2 = evaluation2; }

    /**
     * Assigns an evaluator to the secondary evaluation.
     *
     * @param pId the evaluator identifier
     */
    public void setEvaluator2(Long pId) { this.evaluation2.setEvaluatorId(pId); }

    /**
     * @return true if both evaluations have been completed
     */
    @JsonIgnore
    public boolean isBothEvaluated() { return isEvaluated() && isEvaluated2(); }

    /**
     * @return the process type
     */
    @Override
    public EnumTypeProcess getTypeProcess() { return EnumTypeProcess.DRAFT; }

    /**
     * @return true if the deadline has passed
     */
    @JsonIgnore
    public boolean isExpired() { return new Date().after(deadline); }

    /**
     * @return true if the secondary evaluation is approved
     */
    @JsonIgnore
    public boolean isApproved2() { return isAssigned2() && evaluation2.isApproved(); }

    /**
     * @return true if a secondary evaluator is assigned
     */
    @JsonIgnore
    public boolean isAssigned2() { return evaluation2 != null; }

    /**
     * @return true if the secondary evaluation has been completed
     */
    @JsonIgnore
    public boolean isEvaluated2() { return isAssigned2() && evaluation2.isEvaluated(); }
}
