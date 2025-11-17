package Evaluation_AssignmentService.ProcessEntity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a presentation process.
 * Manages the relationship between a degree work and its assigned juries.
 */
@Entity
@Table(name = "presentation")
public class Presentation {

    /** ID of the related degree work. */
    @Id
    @Column(name = "degreework_id")
    private Long degreeworkId;

    /** List of jury IDs assigned to the presentation. */
    private List<Long> idjurys = new ArrayList<>();

    /** Default constructor. */
    public Presentation() {}

    /**
     * Constructor with one jury.
     * @param degreeworkId degree work ID
     * @param idJury1 ID of the first jury
     */
    public Presentation(Long degreeworkId, Long idJury1) {
        this.degreeworkId = degreeworkId;
        idjurys.add(idJury1);
    }

    /**
     * Constructor with two juries.
     * @param degreeworkId degree work ID
     * @param idJury1 ID of the first jury
     * @param idJury2 ID of the second jury
     */
    public Presentation(Long degreeworkId, Long idJury1, Long idJury2) {
        this.degreeworkId = degreeworkId;
        idjurys.add(idJury1);
        idjurys.add(idJury2);
    }

    /**
     * Constructor with a list of juries.
     * @param degreeworkId degree work ID
     * @param idjurys list of jury IDs
     */
    public Presentation(Long degreeworkId, List<Long> idjurys) {
        this.degreeworkId = degreeworkId;
        this.idjurys = idjurys;
    }

    // -------------------- Getters and Modifiers --------------------

    /** @return degree work ID */
    public Long getDegreeworkId() {
        return degreeworkId;
    }

    /** @return list of jury IDs */
    public List<Long> getIdjurys() {
        return idjurys;
    }

    /**
     * Adds a jury to the presentation if not already included.
     * @param idJury jury ID to add
     * @return current Presentation instance
     */
    public Presentation addjury(Long idJury) {
        if (idjurys.contains(idJury)) {
            // already exists
        }
        if (idjurys.size() > 1) {
            // limit handling
        }
        idjurys.add(idJury);
        return this;
    }

    /**
     * Removes a jury from the presentation if present.
     * @param idJury jury ID to remove
     * @return current Presentation instance
     */
    public Presentation removejury(Long idJury) {
        if (idjurys.contains(idJury)) {
            idjurys.remove(idJury);
        }
        return this;
    }

    /**
     * Replaces the entire jury list.
     * @param idJurys new list of jury IDs
     * @return current Presentation instance
     */
    public Presentation setJurys(List<Long> idJurys) {
        this.idjurys = idJurys;
        return this;
    }
}
