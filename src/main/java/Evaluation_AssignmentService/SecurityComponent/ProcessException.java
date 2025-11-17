package Evaluation_AssignmentService.SecurityComponent;

/**
 * Custom exception used for handling process-related errors.
 */
public class ProcessException extends RuntimeException {
    private final EnumTypeExceptions type;

    /**
     * Creates a new ProcessException with a specific type.
     * @param type the exception type
     */
    public ProcessException(EnumTypeExceptions type) {
        super(type.getMessage());
        this.type = type;
    }

    /**
     * Returns the type of the exception.
     * @return exception type
     */
    public EnumTypeExceptions getType() {
        return type;
    }
}
