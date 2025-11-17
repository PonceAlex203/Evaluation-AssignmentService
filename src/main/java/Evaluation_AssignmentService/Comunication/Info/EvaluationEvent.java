package Evaluation_AssignmentService.Comunication.Info;

import java.io.Serializable;

public class EvaluationEvent implements Serializable {
    private final EnumDegreeWorkStateType type;
    private final Long degreeWorkId;

    public EvaluationEvent(Long degreeWorkId, EnumDegreeWorkStateType type) {
        this.type = type;
        this.degreeWorkId = degreeWorkId;
    }

    public EnumDegreeWorkStateType getType() { return type; }
    public Long getDegreeWorkId() { return degreeWorkId; }
}
