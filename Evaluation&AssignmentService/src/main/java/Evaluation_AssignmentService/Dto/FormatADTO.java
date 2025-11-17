package Evaluation_AssignmentService.Dto;

/**
 * DTO for Format A processes.
 * Extends ProcessDTO and adds company letter path.
 */
public class FormatADTO extends ProcessDTO {
    /** Path to the company's letter document. */
    private String companyLetterPath;

    /** Default constructor. */
    public FormatADTO(){ }

    /**
     * Gets the path of the company letter.
     * @return path as a String
     */
    public String getCompanyLetterPath() {
        return companyLetterPath;
    }

    /**
     * Sets the path of the company letter.
     * @param companyLetterPath path to set
     */
    public void setCompanyLetterPath(String companyLetterPath) {
        this.companyLetterPath = companyLetterPath;
    }
}

