package microservice.ProcessEvaluation.Dtos.Output;

public class FormatAResponseDTO extends ProcessResponseDTO{
    private byte attempts;

    public byte getAttempts() {
        return attempts;
    }

    public void setAttempts(byte attempts) {
        this.attempts = attempts;
    }
}
