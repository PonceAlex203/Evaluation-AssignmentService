package microservice.ProcessEvaluation.Entities.Process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

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
    private Long departmentHeadId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    @Embedded
    private Evaluation evaluation2;

    public Draft() {
        super();
    }

    public Draft(CoreProcess pCore, String pUrl) {
        super(pCore, pUrl);
    }

    @PostLoad
    @PostPersist
    private void initializeDeadline() {
        Calendar vCal = Calendar.getInstance();
        vCal.setTime(this.getCore().getDate());
        vCal.add(Calendar.DAY_OF_YEAR, 90);
        this.deadline = vCal.getTime();
    }
    @JsonIgnore
    public boolean isExpired() {
        return new Date().after(deadline);
    }

    public Date getDeadline() {
        return deadline;
    }

    private void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Evaluation getEvaluation2() {
        return evaluation2;
    }

    public void setEvaluation2(Evaluation evaluation2) {
        this.evaluation2 = evaluation2;
    }

    public Long getDepartmentHeadId() {
        return departmentHeadId;
    }

    public void setDepartmentHeadId(Long departmentHeadId) {
        this.departmentHeadId = departmentHeadId;
    }
    public void setEvaluator2(Long pId){
        this.evaluation2.setEvaluatorId(pId);
    }
    @JsonIgnore
    public boolean isBothAssigned() {
        boolean vFirstAssigned = evaluation != null && evaluation.isAssigned();
        boolean vSecondAssigned = evaluation2 != null && evaluation2.isAssigned();

        return vFirstAssigned && vSecondAssigned;
    }

    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.DRAFT;
    }
    @Override
    @JsonIgnore
    public boolean isEvaluator(Long pIdEvaluator){
        if (super.isEvaluator(pIdEvaluator))
            return true;
        else return evaluation2 != null && evaluation2.getEvaluatorId() == pIdEvaluator;
    }

}
