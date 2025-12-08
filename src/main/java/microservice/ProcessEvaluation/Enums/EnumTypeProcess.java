package microservice.ProcessEvaluation.Enums;


/**
 * Represents the available types of processes in the degree work workflow.
 * Each type contains a display message used for UI or reporting.
 */
public enum EnumTypeProcess {

    DRAFT("ANTEPROYECTO "),
    FORMAT_A("FORMATO A");

    private final String message;

    /**
     * Creates a process type with its display message.
     *
     * @param pNameMessage the associated message
     */
    EnumTypeProcess(String pNameMessage) {
        this.message = pNameMessage;
    }

    /**
     * @return the display message associated with the process type
     */
    public String getMessage() {
        return message;
    }
}

