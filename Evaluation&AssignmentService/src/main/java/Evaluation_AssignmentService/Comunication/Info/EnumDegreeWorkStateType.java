package Evaluation_AssignmentService.Comunication.Info;

public enum EnumDegreeWorkStateType {
    FORMAT_A,          // El director está creando el Formato A
    FORMAT_A_SUBMITTED,      // El Formato A fue enviado para revisión
    FORMAT_A_UNDER_REVIEW,   // El coordinador está revisando el documento
    FORMAT_A_REJECTED,       // El coordinador rechazó el formato
    FORMAT_A_APPROVED,       // El Formato A fue aprobado
    FORMAT_A_FAILED,         // Rechazado definitivamente tras 3 intentos

    // === FASE 2: ANTEPROYECTO ===
    DRAFT,        // El director crea el anteproyecto
    DRAFT_SUBMITTED,    // Enviado para revisión
    DRAFT_UNDER_REVIEW, // En revisión por el coordinador
    DRAFT_REJECTED,     // Coordinador lo rechaza
    DRAFT_APPROVED,     // Coordinador aprueba el anteproyecto

    // === FASE 3: ASIGNACIÓN Y DEFENSA ===
    JURY_ASSIGNED,           // Jurados asignados por el jefe de departamento

    // === FASE FINAL ===
    COMPLETED,               // Sustentación realizada exitosamente
    FAILED;
}