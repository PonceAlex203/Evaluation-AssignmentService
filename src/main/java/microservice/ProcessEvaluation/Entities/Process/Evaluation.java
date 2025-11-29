package microservice.ProcessEvaluation.Entities.Process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.EnumType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;

import java.util.Date;

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

    public Evaluation() {
    }

    public Evaluation(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
        assignmentDate = new Date();
    }
    @JsonIgnore
    public boolean isEvaluated() {return evaluationStatus != EnumProcessStatus.PENDING;}
    @JsonIgnore
    public boolean isApproved(){
        return evaluationStatus == EnumProcessStatus.APPROVED;
    }
    @JsonIgnore
    public boolean isRejected(){
        return evaluationStatus == EnumProcessStatus.REJECTED;
    }

    public Date getAssignmentDate() { return assignmentDate; }
    private void setAssignmentDate(Date assignmentDate) { this.assignmentDate = assignmentDate; }

    public Date getEvaluationDate() { return evaluationDate; }
    private void setEvaluationDate(Date evaluationDate) { this.evaluationDate = evaluationDate; }

    public Long getEvaluatorId() { return evaluatorId; }

    public void evaluate(EnumProcessStatus pNewStatus, String pComment) {
        this.comment = pComment;
        this.evaluationStatus = pNewStatus;
        this.evaluationDate = new Date();
    }
    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
        this.assignmentDate = new Date();
    }

    public String getComment() { return comment; }
    private void setComment(String comment) { this.comment = comment; }

    public EnumProcessStatus getEvaluationStatus() { return evaluationStatus; }
    private void setEvaluationStatus(EnumProcessStatus evaluationStatus) { this.evaluationStatus = evaluationStatus; }
}