package Evaluation_AssignmentService.Enum;

/**
 * Enumeration of process types.
 * Identifies the type of process handled in the system.
 */
public enum EnumTypeProcess {

    DRAFT("ANTEPROYECTO "),
    FORMAT_A("FORMATO A");

    private final String message;

    EnumTypeProcess(String pNameMessage) {
        this.message = pNameMessage;
    }

    public String getMessage() {
        return message;
    }
}

