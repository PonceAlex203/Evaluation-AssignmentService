package Evaluation_AssignmentService.SecurityComponent;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "existing_degree_works")
public class DegreeWorkIds {

    @Id
    private Long id;

    public DegreeWorkIds(){ }
    public DegreeWorkIds(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

