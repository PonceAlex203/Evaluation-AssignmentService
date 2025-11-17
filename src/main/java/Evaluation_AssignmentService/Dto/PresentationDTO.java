package Evaluation_AssignmentService.Dto;

import java.util.List;

/**
 * DTO for handling presentation data.
 * Contains degree work ID and a list of jury IDs.
 */
public class PresentationDTO {
    /** ID of the degree work. */
    private Long idDegreeWork;

    /** List of IDs of jurors. */
    private List<Long> idjurys;

    /**
     * Sets the degree work ID.
     * @param idDegreeWork ID to set
     */
    public void setIdDegreeWork(Long idDegreeWork) {
        this.idDegreeWork = idDegreeWork;
    }

    /**
     * Sets the list of juror IDs.
     * @param idjurys List of IDs to set
     */
    public void setIdjurys(List<Long> idjurys) {
        this.idjurys = idjurys;
    }

    /**
     * Gets the degree work ID.
     * @return degree work ID
     */
    public Long getIdDegreeWork() {
        return idDegreeWork;
    }

    /**
     * Gets the list of juror IDs.
     * @return list of juror IDs
     */
    public List<Long> getIdjurys() {
        return idjurys;
    }
}
