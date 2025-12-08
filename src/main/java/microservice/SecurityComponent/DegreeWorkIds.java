package microservice.SecurityComponent;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a valid degree work identifier that is allowed
 * to be associated with processes. This entity is used as a
 * control list to validate whether a process can be created
 * for a specific degree work.
 */
@Entity
@Table(name = "existing_degree_works")
public class DegreeWorkIds {

    @Id
    private Long id;

    /**
     * Default constructor.
     */
    public DegreeWorkIds(){ }

    /**
     * Constructs a degree work identifier entry.
     * @param id the degree work identifier
     */
    public DegreeWorkIds(Long id) {
        this.id = id;
    }

    /**
     * @return the degree work identifier
     */
    public Long getId() { return id; }

    /**
     * Sets the degree work identifier.
     * @param id the degree work identifier
     */
    public void setId(Long id) { this.id = id; }
}

