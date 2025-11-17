package Evaluation_AssignmentService.ProcessEntity;

import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.Enum.EnumTypeProcess;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Abstract base entity for all process types.
 * Provides common attributes such as date, status, and comments.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseProcess {

    /** Unique identifier for the process. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProcess;

    /** ID of the associated degree work. */
    @Column(name = "degreework_id")
    protected Long degreeworkId;

    /** Date when the process was created. */
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date = new Date();

    /** File or resource URL related to the process. */
    protected String url = "";

    /** Current status of the process. */
    protected EnumProcessStatus status = EnumProcessStatus.PENDING;

    /** Optional comments for the process. */
    protected String comments;

    /** Default constructor that sets the creation date. */
    protected BaseProcess() {
        this.date = new Date();
    }

    /**
     * Constructor with degree work ID.
     * @param degreeworkId ID of the related degree work
     */
    public BaseProcess(Long degreeworkId) {
        this.degreeworkId = degreeworkId;
    }

    /**
     * Constructor with degree work ID and URL.
     * @param degreeworkId ID of the related degree work
     * @param url URL associated with the process
     */
    public BaseProcess(Long degreeworkId, String url) {
        this.degreeworkId = degreeworkId;
        this.url = url;
    }

    // -------------------- Getters and Setters --------------------

    /** @return ID of the related degree work */
    public Long getDegreeworkId() { return degreeworkId; }

    /** @param degreeworkId ID of the related degree work */
    public void setDegreeworkId(Long degreeworkId) { this.degreeworkId = degreeworkId; }

    /** @return date when the process was created */
    public Date getDate() { return date; }

    /** @param date creation or update date */
    public void setDate(Date date) { this.date = date; }

    /** @return URL of the process resource */
    public String getUrl() { return url; }

    /** @param url resource URL to set */
    public void setUrl(String url) { this.url = url; }

    /** @return current status of the process */
    public EnumProcessStatus getStatus() { return status; }

    /** @param status new process status */
    public void setStatus(EnumProcessStatus status) { this.status = status; }

    /** @return comments for the process */
    public String getComments() { return comments; }

    /** @param comments comments to set */
    public void setComments(String comments) { this.comments = comments; }

    public abstract EnumTypeProcess getTypeProcess();
}