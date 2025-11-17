package Evaluation_AssignmentService.SecurityComponent;

import java.util.Date;

/**
 * Represents an error response sent to the client when an exception occurs.
 */
public final class ErrorResponse {
    private final EnumTypeExceptions typeException;
    private final String message;
    private final Date date;

    /**
     * Creates a new ErrorResponse instance.
     * @param typeException the type of exception
     * @param date the date when the error occurred
     * @param message the detailed error message
     */
    public ErrorResponse(EnumTypeExceptions typeException, Date date, String message) {
        this.typeException = typeException;
        this.date = date;
        this.message = message;
    }

    /**
     * Returns the error message.
     * @return error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the date of the error.
     * @return date of the error
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the type of exception.
     * @return type of exception
     */
    public EnumTypeExceptions getTypeException() {
        return typeException;
    }
}
