package Evaluation_AssignmentService.Dto;

/**
 * Abstract base class for all process-related DTOs.
 * Holds common properties for processes.
 */
public abstract class ProcessDTO {
    /** ID of the related degree work. */
    protected Long degreeWorkId;

    /** URL associated with the process. */
    protected String url;

    /** Default constructor. */
    public ProcessDTO() { }

    /**
     * Gets the URL of the process.
     * @return URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the process.
     * @param url URL to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the ID of the degree work.
     * @return degree work ID
     */
    public Long getDegreeWorkId() {
        return degreeWorkId;
    }

    /**
     * Sets the ID of the degree work.
     * @param degreeWorkId ID to set
     */
    public void setDegreeWorkId(Long degreeWorkId) {
        this.degreeWorkId = degreeWorkId;
    }
}
