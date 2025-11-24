package microservice.ProcessEvaluation.Dtos.Output;

/**
 * Base response DTO for process operations with URL
 */
public class ProcessResponseDTO extends CoreProcessResponseDTO {
    protected String url;

    public ProcessResponseDTO() { }

    /**
     * @return the document URL
     */
    public String getUrl() { return url;}
    public void setUrl(String url) { this.url = url;}
}