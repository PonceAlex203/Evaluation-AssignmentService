package microservice.ProcessEvaluation.Dtos.Output;

public class ProcessResponseDTO extends CoreProcessResponseDTO {
    protected String url;

    public ProcessResponseDTO() {
    }

    public ProcessResponseDTO(String url) {
        this.url = url;
    }
    public String getUrl() { return url;}
    public void setUrl(String url) { this.url = url;}
}
