package microservice.ProcessEvaluation.Entities.Process;

import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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
    }

    public FormatA(CoreProcess pCore, String pUrl) {
        super(pCore, pUrl);
    }

    public byte getAttempts(){ return this.attempts; }
    public void setAttempts(byte attempts) {
        this.attempts = attempts;
    }

    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.FORMAT_A;
    }

    @Override
    public void setStatus(EnumProcessStatus status) {
        super.setStatus(status);
        if(this.status.equals(EnumProcessStatus.REJECTED))
            this.attempts++;
    }
}
