package microservice.SecurityComponent;

import org.springframework.http.HttpStatus;

/**
 * Defines the different exception types used in the process
 * workflow, each associated with an error code, a descriptive
 * message, and the corresponding HTTP status.
 */
public enum EnumTypeExceptions {

    PROCESS_NOT_SUPPORTED("P-405", "Ese tipo de proceso no esta soportado", HttpStatus.METHOD_NOT_ALLOWED),
    EXISTING_ID("P-409", "Ese ID ya existe para este proceso", HttpStatus.CONFLICT),
    NOT_FOUND("P-404", "Proceso no encontrado", HttpStatus.NOT_FOUND),
    PREVIOUS_PROCESS_NOT_APPROVED("P-412", "El proceso anterior no ha sido aprobado y no puede continuar hasta que sea aprobado", HttpStatus.PRECONDITION_FAILED),
    EXPIRED_TIME("P-410", "El tiempo disponible ha finalizado", HttpStatus.GONE),
    NON_MODIFICABLE_PROCESS("P-403", "El proceso actual solo puede ser resubido cuando se encuentra rechazado", HttpStatus.FORBIDDEN),
    DEGREEWORKID_NOT_FOUND("P-404", "El id del trabajo de grado solicitado no existe, no puede asociarlo", HttpStatus.NOT_FOUND),
    DEGREEWORKID_EXISTING("P-400", "El id del trabajo de grado solicitado ya existe, no puede volver a asociarlo", HttpStatus.CONFLICT),
    PREVIOUSLY_ASSIGNED("P-400", "El evaluador ya se encuentra asignado a este proceso, no puede volver a asignarlo, seleccione otro", HttpStatus.CONFLICT),
    IDENTICAL_EVALUATORS_IDS("P-400","Los ids de los jurados son los mismos, debe escoger 2 diferentes", HttpStatus.BAD_REQUEST);

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;

    /**
     * Constructs an exception type descriptor.
     * @param pCode the internal process error code
     * @param message the human-readable error message
     * @param pHttpStatus the HTTP status associated with the error
     */
    EnumTypeExceptions(String pCode, String message, HttpStatus pHttpStatus) {
        this.message = message;
        this.code = pCode;
        this.httpStatus = pHttpStatus;
    }

    /**
     * @return the descriptive error message
     */
    public String getMessage() { return message; }
}
