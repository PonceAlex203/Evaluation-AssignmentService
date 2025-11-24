package microservice.Comunication.Info;

/**
 * Represents the possible states of a degree work process
 */
public enum EnumDegreeWorkStateType {
    /** Director is creating Format A */
    FORMAT_A,
    /** Format A submitted for review */
    FORMAT_A_SUBMITTED,
    /** Format A rejected by coordinator */
    FORMAT_A_REJECTED,
    /** Format A approved by coordinator */
    FORMAT_A_APPROVED,
    /** Permanently rejected after 3 attempts */
    FORMAT_A_FAILED,

    /** Director creates the draft */
    DRAFT,
    /** Draft submitted for review */
    DRAFT_SUBMITTED,
    /** Jury assigned by department head */
    JURY_ASSIGNED,
    /** First jury evaluation phase */
    FIRST_JURY_EVALUATION,
    /** Draft rejected by coordinator */
    DRAFT_REJECTED,
    /** Draft approved by coordinator */
    DRAFT_APPROVED,

    /** Successfully completed defense */
    COMPLETED,
    /** Failed degree work */
    FAILED;
}