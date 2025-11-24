package microservice.ProcessEvaluation.Entities.Process;

import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Enums.EnumAssignmentStatus;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


/**
 * Abstract base entity for all process types.
 * Provides common attributes such as date, generalEvaluationStatus, and comments.
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long idProcess;

    @Embedded
    protected CoreProcess core;

    protected String url;

    @Enumerated(EnumType.STRING)
    protected EnumProcessStatus generalEvaluationStatus = EnumProcessStatus.PENDING;

    @Enumerated(EnumType.STRING)
    protected EnumAssignmentStatus assignmentStatus = EnumAssignmentStatus.UNASSIGNED;
    @Embedded
    protected Evaluation evaluation;

    protected BaseProcess() {
    }

    protected BaseProcess(CoreProcess pCore, String pUrl) {
        core = pCore;
        this.url = pUrl;
    }

    // -------------------- Getters and Setters --------------------

    public Long getIdProcess() {
        return idProcess;
    }

    private void setIdProcess(Long idProcess) {
        this.idProcess = idProcess;
    }

    public CoreProcess getCore() {
        return core;
    }

    private void setCore(CoreProcess core) {
        this.core = core;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Evaluation getEvaluation() { return evaluation;}

    public void setEvaluation(Evaluation evaluation) {this.evaluation = evaluation;}

    @JsonIgnore
    public Long getDegreeworkId(){
        return core.getDegreeWorkId();
    }
    public EnumProcessStatus getGeneralEvaluationStatus(){ return this.generalEvaluationStatus; }
    public void setGeneralEvaluationStatus(EnumProcessStatus pNewStatus) { this.generalEvaluationStatus = pNewStatus;}
    public void setEvaluator(Long pIdEvaluator){evaluation.setEvaluatorId(pIdEvaluator);}
    @JsonIgnore
    public boolean isApproved(){return evaluation != null && evaluation.isApproved();}
    @JsonIgnore
    public boolean isFullAssigned() {return assignmentStatus == EnumAssignmentStatus.ASSIGNED; }
    @JsonIgnore
    public boolean isUnassigned(){ return assignmentStatus == EnumAssignmentStatus.UNASSIGNED; }
    @JsonIgnore
    public boolean isEvaluated(){return evaluation != null && evaluation.isEvaluated(); }
    @JsonIgnore
    public boolean isAssigned(){ return evaluation != null; }
    @JsonIgnore
    public boolean isEvaluator(Long pIdEvaluator){
        if(isAssigned())
            return evaluation.getEvaluatorId() == pIdEvaluator;
        else return false;
    }
    public EnumAssignmentStatus getAssignmentStatus() {return assignmentStatus;}
    public void setAssignmentStatus(EnumAssignmentStatus assignmentStatus) {this.assignmentStatus = assignmentStatus;}
    // -------------------- Abstract methods --------------------
    @JsonIgnore
    public abstract EnumTypeProcess getTypeProcess();
}