package microservice.ProcessEvaluation.Dtos.Input;

/**
 * Abstract base class for all process-related DTOs.
 * Holds common properties for processes.
 */
public class ProcessDTO extends CoreProcessDTO{
    protected String url;

    public ProcessDTO() {super();}

    public ProcessDTO(Long pId) {
        super(pId);
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
