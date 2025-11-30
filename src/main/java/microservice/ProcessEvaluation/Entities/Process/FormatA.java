package microservice.ProcessEvaluation.Entities.Process;

import jakarta.persistence.*;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;

/**
 * Entity representing a Format A process.
 * Contains the company letter and submission attempt tracking.
 */
@Entity
@Table(
        name = "format_a",
        uniqueConstraints = @UniqueConstraint(columnNames = "degreework_id")
)
public class FormatA extends BaseProcess {
    private byte attempts = 1;

    public FormatA() {
        super();
        assignDefaultEvaluator();
    }

    public FormatA(CoreProcess pCore, String pUrl) {
        super(pCore, pUrl);
        assignDefaultEvaluator();
    }

    public byte getAttempts(){ return this.attempts; }
    private void setAttempts(byte attempts) {
        this.attempts = attempts;
    }
    public void increaseAttempts(){ attempts++; }
    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.FORMAT_A;
    }

    private void assignDefaultEvaluator(){
        this.evaluation = new Evaluation(null);
        this.generalStatus = EnumDegreeWorkStateType.FORMAT_A_SUBMITTED;
    }
}
