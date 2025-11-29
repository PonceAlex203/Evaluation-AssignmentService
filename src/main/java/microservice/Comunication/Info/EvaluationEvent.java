package microservice.Comunication.Info;

import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;

import java.io.Serializable;

/**
 * Represents an evaluation event in the degree work process
 */
public class EvaluationEvent implements Serializable {
    private final EnumDegreeWorkStateType type;
    private final Long degreeWorkId;

    /**
     * Constructs an evaluation event
     * @param degreeWorkId the degree work identifier
     * @param type the evaluation state type
     */
    public EvaluationEvent(Long degreeWorkId, EnumDegreeWorkStateType type) {
        this.type = type;
        this.degreeWorkId = degreeWorkId;
    }

    /**
     * @return the state type
     */
    public EnumDegreeWorkStateType getType() { return type; }

    /**
     * @return the degree work identifier
     */
    public Long getDegreeWorkId() { return degreeWorkId; }
}