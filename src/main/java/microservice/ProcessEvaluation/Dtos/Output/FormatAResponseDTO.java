package microservice.ProcessEvaluation.Dtos.Output;

/**
 * Response DTO for Format A processes
 */
public class FormatAResponseDTO extends ProcessResponseDTO{
    private byte attempts;

    /**
     * @return the number of submission attempts
     */
    public byte getAttempts() {
        return attempts;
    }
    public void setAttempts(byte attempts) {
        this.attempts = attempts;
    }
}
