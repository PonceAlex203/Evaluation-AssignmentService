package Evaluation_AssignmentService.ProcessEntity;

import Evaluation_AssignmentService.Enum.EnumTypeProcess;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entity representing a Format A process.
 * Contains the company letter and submission attempt tracking.
 */
@Entity
@Table(
        name = "format_a",
        uniqueConstraints = @UniqueConstraint(columnNames = "degreework_id")
)
public class FormatA extends BaseProcess {

    /** Path to the company letter document. */
    private String companyLetterPath;

    /** Number of submission attempts. */
    private byte attempts = 1;

    /** Default constructor. */
    protected FormatA() { super(); }

    /**
     * Constructor with degree work ID.
     * @param pDegreeWorkId degree work ID
     */
    public FormatA(long pDegreeWorkId) { super(pDegreeWorkId); }

    /**
     * Constructor with degree work ID and URL.
     * @param degreeworkId degree work ID
     * @param url process document URL
     */
    public FormatA(Long degreeworkId, String url) {
        super(degreeworkId, url);
    }

    /**
     * Constructor with degree work ID, URL, and company letter path.
     * @param degreeworkId degree work ID
     * @param url process document URL
     * @param companyLetterPath path to company letter file
     */
    public FormatA(Long degreeworkId, String url, String companyLetterPath) {
        super(degreeworkId, url);
        this.companyLetterPath = companyLetterPath;
    }

    /**
     * Gets the number of attempts made.
     * @return number of attempts
     */
    public byte getAttempts(){ return this.attempts; }

    /**
     * Gets the company letter path.
     * @return path as string
     */
    public String getCompanyLetterPath(){ return this.companyLetterPath; }

    /**
     * Sets the company letter path.
     * @param pNewCompanyLetterPath new path value
     */
    public void setCompanyLetterPath(String pNewCompanyLetterPath) { this.companyLetterPath = pNewCompanyLetterPath; }

    /**
     * Sets the number of attempts.
     * @param attempts new attempts value
     */
    public void setAttempts(byte attempts) {
        this.attempts = attempts;
    }

    /**
     * Increases the number of attempts by one.
     */
    public void increaseAttempts(){ this.attempts++; }

    @Override
    public EnumTypeProcess getTypeProcess() {
        return EnumTypeProcess.FORMAT_A;
    }

}
