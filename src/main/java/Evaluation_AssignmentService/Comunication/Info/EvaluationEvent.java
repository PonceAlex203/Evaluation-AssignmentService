package Evaluation_AssignmentService.Comunication.Info;

import java.io.Serializable;

public class EvaluationEvent implements Serializable {
    private EnumDegreeWorkStateType type;
    private Long degreeWorkId;

    public EvaluationEvent() {}

    public EvaluationEvent(Long degreeWorkId, EnumDegreeWorkStateType type) {
        this.type = type;
        this.degreeWorkId = degreeWorkId;
    }

    public EnumDegreeWorkStateType getType() { return type; }
    public Long getDegreeWorkId() { return degreeWorkId; }
}
