package microservice.ProcessEvaluation.Entities.Process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.EnumType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;

import java.util.Date;

/**
 * Represents the evaluation metadata of a process,
 * including evaluator, comments, status and relevant timestamps.
 */
@Embeddable
public class Evaluation {

    @Temporal(TemporalType.TIMESTAMP)
    private Date assignmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date evaluationDate;

    private Long evaluatorId;
    private String comment;

    @Enumerated(EnumType.STRING)
    private EnumProcessStatus evaluationStatus = EnumProcessStatus.PENDING;

    /**
     * Default constructor for JPA.
     */
    public Evaluation() {}

    /**
     * Creates an evaluation with the assigned evaluator.
     *
     * @param evaluatorId the evaluator identifier
     */
    public Evaluation(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
        assignmentDate = new Date();
    }

    /**
     * @return the evaluator assignment date
     */
    public Date getAssignmentDate() { return assignmentDate; }

    private void setAssignmentDate(Date assignmentDate) { this.assignmentDate = assignmentDate; }

    /**
     * @return the date on which the evaluation was completed
     */
    public Date getEvaluationDate() { return evaluationDate; }

    private void setEvaluationDate(Date evaluationDate) { this.evaluationDate = evaluationDate; }

    /**
     * @return the evaluator identifier
     */
    public Long getEvaluatorId() { return evaluatorId; }

    /**
     * Marks the evaluation as failed.
     */
    public void setFailedStatus() { evaluationStatus = EnumProcessStatus.FAILED; }

    /**
     * @return the evaluator comment
     */
    public String getComment() { return comment; }

    private void setComment(String comment) { this.comment = comment; }

    /**
     * @return the current evaluation status
     */
    public EnumProcessStatus getEvaluationStatus() { return evaluationStatus; }

    private void setEvaluationStatus(EnumProcessStatus evaluationStatus) { this.evaluationStatus = evaluationStatus; }

    /**
     * Assigns an evaluator without modifying any evaluation metadata.
     *
     * @param pEvaluatorId the evaluator identifier
     */
    public void addOnlyEvaluator(Long pEvaluatorId) { evaluatorId = pEvaluatorId; }

    /**
     * @return true if the process has been evaluated
     */
    @JsonIgnore
    public boolean isEvaluated() { return evaluationStatus != EnumProcessStatus.PENDING; }

    /**
     * @return true if the evaluation is approved
     */
    @JsonIgnore
    public boolean isApproved() { return evaluationStatus == EnumProcessStatus.APPROVED; }

    /**
     * @return true if the evaluation is rejected
     */
    @JsonIgnore
    public boolean isRejected() { return evaluationStatus == EnumProcessStatus.REJECTED; }

    /**
     * Evaluates the process by setting status, comment and evaluation date.
     *
     * @param pNewStatus the new evaluation status
     * @param pComment the evaluator comment
     */
    public void evaluate(EnumProcessStatus pNewStatus, String pComment) {
        this.comment = pComment;
        this.evaluationStatus = pNewStatus;
        this.evaluationDate = new Date();
    }

    /**
     * Assigns or reassigns an evaluator, updating the assignment timestamp.
     *
     * @param evaluatorId the evaluator identifier
     */
    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
        this.assignmentDate = new Date();
    }

    /**
     * Resets evaluation values when a failed evaluation must be redone.
     */
    public void resetFailedEvaluationAttributes() {
        evaluationStatus = EnumProcessStatus.PENDING;
        evaluationDate = null;
        comment = null;
    }
}
