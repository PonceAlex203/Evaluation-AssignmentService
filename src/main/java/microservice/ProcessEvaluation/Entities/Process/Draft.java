package microservice.ProcessEvaluation.Entities.Process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
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
    private Long departmentHeadId = 1L;

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
    public boolean isPartialAssignment() {return generalStatus == EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED;}
    @JsonIgnore
    public boolean isBothEvaluated(){return isEvaluated() && isEvaluated2();}
    @Override
    public EnumTypeProcess getTypeProcess() {return EnumTypeProcess.DRAFT;}
    @JsonIgnore
    public boolean isApproved2(){return isAssigned2() && evaluation2.isApproved();}
    @JsonIgnore
    public boolean isAssigned2(){return evaluation2 != null;}
    @JsonIgnore
    public boolean isEvaluated2(){ return isAssigned2() && evaluation2.isEvaluated(); }
    @JsonIgnore
    public boolean isEvaluator2(Long pIdEvaluator){
        if(isAssigned2())
            return evaluation2.getEvaluatorId() == pIdEvaluator;
        else return false;
    }
    @JsonIgnore
    public boolean isAnyEvaluator(Long pIdEvaluator) {
        return isEvaluator(pIdEvaluator) || isEvaluator2(pIdEvaluator);
    }

}
