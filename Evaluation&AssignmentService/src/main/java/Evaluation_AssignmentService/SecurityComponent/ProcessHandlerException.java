package Evaluation_AssignmentService.SecurityComponent;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

/**
 * Handles exceptions globally for process-related controllers.
 */
@RestControllerAdvice
public class ProcessHandlerException {

    /**
     * Handles ProcessException and returns a standardized error response.
     * @param pException the exception thrown
     * @return ResponseEntity containing error details and HTTP status
     */
    @ExceptionHandler(ProcessException.class)
    public ResponseEntity<ErrorResponse> handleProcessException(ProcessException pException) {
        ErrorResponse error = new ErrorResponse(pException.getType(), new Date(), pException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

