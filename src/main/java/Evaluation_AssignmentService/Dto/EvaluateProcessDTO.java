package Evaluation_AssignmentService.Dto;

import Evaluation_AssignmentService.Enum.EnumProcessStatus;

/**
 * DTO for evaluating a process.
 * Contains comment and new status to update.
 */
public class EvaluateProcessDTO {
    /** Comment related to the evaluation. */
    private String Comment;

    /** New status of the process. */
    private EnumProcessStatus NewStatus;

    /** Default constructor. */
    public EvaluateProcessDTO() { }

    /**
     * Constructor with parameters.
     * @param comment Comment for the evaluation
     * @param newStatus New process status
     */
    public EvaluateProcessDTO(String comment, EnumProcessStatus newStatus) {
        Comment = comment;
        NewStatus = newStatus;
    }

    /**
     * Gets the evaluation comment.
     * @return comment as String
     */
    public String getComment() {
        return Comment;
    }

    /**
     * Sets the evaluation comment.
     * @param comment comment to set
     */
    public void setComment(String comment) {
        this.Comment = comment;
    }

    /**
     * Gets the new status of the process.
     * @return new process status
     */
    public EnumProcessStatus getNewStatus() {
        return NewStatus;
    }

    /**
     * Sets the new status of the process.
     * @param newStatus new status to set
     */
    public void setNewStatus(EnumProcessStatus newStatus) {
        this.NewStatus = newStatus;
    }
}

