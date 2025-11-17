package Evaluation_AssignmentService.SecurityComponent;

import org.springframework.http.HttpStatus;

/**
 * Enum that defines different types of process-related exceptions.
 */
public enum EnumTypeExceptions {
    INVALID_NEW_STATUS("P-400", "El nuevo estado es invalido, solo puede ser Approved o Rejected", HttpStatus.BAD_REQUEST),
    PROCESS_NOT_SUPPORTED("P-405", "Ese tipo de proceso no esta soportado", HttpStatus.METHOD_NOT_ALLOWED),
    EXISTING_ID("P-409", "Ese ID ya existe para este proceso", HttpStatus.CONFLICT),
    NOT_FOUND("P-404", "Proceso no encontrado", HttpStatus.NOT_FOUND),
    LIMIT_ATTEMPT_EXCEEDED("P-429", "El limite de intentos ha sido excedido, el proceso ha sido rechazado definitivamente", HttpStatus.TOO_MANY_REQUESTS),
    PREVIOUS_PROCESS_NOT_APPROVED("P-412", "El proceso anterior no ha sido aprobado y no puede continuar hasta que sea aprobado", HttpStatus.PRECONDITION_FAILED),
    PREVIOUS_PROCESS_NOT_SUMBITTED("P-412", "El proceso anterior no ha sido cargado; no puede continuar hasta que sea cargado y aprobado", HttpStatus.PRECONDITION_FAILED),
    PROCESS_APPROVED("P-409", "El proceso actual esta aprobado, no puede modificarlo", HttpStatus.CONFLICT),
    EXPIRED_TIME("P-410", "El tiempo disponible ha finalizado", HttpStatus.GONE),
    NON_MODIFICABLE_PROCESS("P-403", "El proceso actual solo puede ser resubido cuando se encuentra rechazado", HttpStatus.FORBIDDEN),
    PROCESS_NOT_PENDING("P-409", "El proceso actual no esta en estado pendiente y no puede ser evaluado", HttpStatus.CONFLICT),
    DEGREEWORKID_NOT_FOUND("P-404","El id del trabajo de grado solicitado no existe, no puede asociarlo",HttpStatus.NOT_FOUND),
    DEGREEWORKID_EXISTING("P-400","El id del trabajo de grado solicitado ya existe, no puede volver a asociarlo",HttpStatus.CONFLICT),
    PROCESS_FAILED("P-423", "El proceso actual ha sido rechazado definitivamente", HttpStatus.LOCKED),
    NULL_PARAMETER("P-400", "Parametro nulo", HttpStatus.BAD_REQUEST);

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    /**
     * Constructs a new EnumTypeExceptions with the given message.
     * @param message exception message
     */
    EnumTypeExceptions(String pCode,String message, HttpStatus pHttpStatus) {
        this.message = message;
        this.code = pCode;
        this.httpStatus = pHttpStatus;
    }

    /**
     * Returns the message of the exception.
     * @return exception message
     */
    public String getMessage() { return message; }
}