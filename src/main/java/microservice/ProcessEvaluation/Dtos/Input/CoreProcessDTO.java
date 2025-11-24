package microservice.ProcessEvaluation.Dtos.Input;

public abstract class CoreProcessDTO {
    private Long degreeWorkId;

    public CoreProcessDTO() {}

    public CoreProcessDTO(Long degreeWorkId) {
        this.degreeWorkId = degreeWorkId;
    }

    public Long getDegreeWorkId() {
        return degreeWorkId;
    }

    public void setDegreeWorkId(Long degreeWorkId) {
        this.degreeWorkId = degreeWorkId;
    }
}
