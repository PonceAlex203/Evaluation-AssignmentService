package microservice.ProcessEvaluation.Entities.Process;

import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Table;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;


/**
 * Represents the Format A process of a degree work.
 * Includes validation attempts and a default evaluator.
 */
@Entity
@Table(
        name = "format_a",
        uniqueConstraints = @UniqueConstraint(columnNames = "degreework_id")
)
public class FormatA extends BaseProcess {

    private byte attempts = 1;

    /**
     * Default constructor used by JPA.
     * Assigns a default evaluator and initial status.
     */
    public FormatA() {
        super();
        assignDefaultEvaluator();
    }

    /**
     * Creates a Format A process with core information and a URL.
     *
     * @param pCore the core process information
     * @param pUrl the associated file URL
     */
    public FormatA(CoreProcess pCore, String pUrl) {
        super(pCore, pUrl);
        assignDefaultEvaluator();
    }

    /**
     * @return the number of evaluation attempts
     */
    public byte getAttempts() { return this.attempts; }

    private void setAttempts(byte attempts) { this.attempts = attempts; }

    /**
     * Increases the failed attempt counter.
     */
    public void increaseAttempts() { attempts++; }

    /**
     * @return the process type
     */
    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.FORMAT_A;
    }

    /**
     * Assigns a default evaluator and initial state.
     */
    private void assignDefaultEvaluator() {
        this.evaluation = new Evaluation(null);
        this.generalStatus = EnumDegreeWorkStateType.FORMAT_A_SUBMITTED;
    }
}

