package microservice.ProcessEvaluation.Entities.Process;

import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
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
 * Abstract base entity representing a generic process linked to a degree work.
 * Contains common metadata such as core information, evaluation and status.
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
    protected EnumDegreeWorkStateType generalStatus;

    @Embedded
    protected Evaluation evaluation;

    /**
     * Protected constructor used by JPA.
     */
    protected BaseProcess() {}

    /**
     * Constructs a process with its core information and URL.
     *
     * @param pCore the core process information
     * @param pUrl the associated file URL
     */
    protected BaseProcess(CoreProcess pCore, String pUrl) {
        core = pCore;
        this.url = pUrl;
    }

    /**
     * @return the process identifier
     */
    public Long getIdProcess() { return idProcess; }

    private void setIdProcess(Long idProcess) { this.idProcess = idProcess; }

    /**
     * @return the core process information
     */
    public CoreProcess getCore() { return core; }

    private void setCore(CoreProcess core) { this.core = core; }

    /**
     * @return the associated file URL
     */
    public String getUrl() { return url; }

    /**
     * Sets the file URL.
     *
     * @param url the new URL
     */
    public void setUrl(String url) { this.url = url; }

    /**
     * @return the evaluation object
     */
    public Evaluation getEvaluation() { return evaluation; }

    /**
     * Sets the process evaluation.
     *
     * @param evaluation the new evaluation
     */
    public void setEvaluation(Evaluation evaluation) { this.evaluation = evaluation; }

    /**
     * @return the current general status of the process
     */
    public EnumDegreeWorkStateType getGeneralStatus() { return generalStatus; }

    /**
     * Sets the general process status.
     *
     * @param generalStatus the new status
     */
    public void setGeneralStatus(EnumDegreeWorkStateType generalStatus) { this.generalStatus = generalStatus; }

    /**
     * @return the degree work identifier
     */
    @JsonIgnore
    public Long getDegreeworkId() { return core.getDegreeWorkId(); }

    /**
     * Assigns an evaluator to the process.
     *
     * @param pIdEvaluator the evaluator identifier
     */
    public void setEvaluator(Long pIdEvaluator) { evaluation.setEvaluatorId(pIdEvaluator); }

    /**
     * @return true if the process evaluation is approved
     */
    @JsonIgnore
    public boolean isApproved() { return evaluation != null && evaluation.isApproved(); }

    /**
     * @return true if the process has been evaluated
     */
    @JsonIgnore
    public boolean isEvaluated() { return evaluation != null && evaluation.isEvaluated(); }

    /**
     * @return true if the process has an assigned evaluator
     */
    @JsonIgnore
    public boolean isAssigned() { return evaluation != null; }

    /**
     * Checks if the given evaluator is assigned to this process.
     *
     * @param pIdEvaluator the evaluator identifier
     * @return true if the evaluator matches
     */
    @JsonIgnore
    public boolean isEvaluator(Long pIdEvaluator) {
        if(isAssigned())
            return evaluation.getEvaluatorId() == pIdEvaluator;
        else return false;
    }

    /**
     * @return the type of the process
     */
    @JsonIgnore
    public abstract EnumTypeProcess getTypeProcess();
}
