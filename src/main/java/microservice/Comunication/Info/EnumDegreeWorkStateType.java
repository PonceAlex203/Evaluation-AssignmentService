package microservice.Comunication.Info;

public enum EnumDegreeWorkStateType {
    FORMAT_A,          // El director está creando el Formato A
    FORMAT_A_SUBMITTED,      // El Formato A fue enviado para revisión
    FORMAT_A_REJECTED,       // El coordinador rechazó el formato
    FORMAT_A_APPROVED,       // El Formato A fue aprobado
    FORMAT_A_FAILED,         // Rechazado definitivamente tras 3 intentos

    // === FASE 2: ANTEPROYECTO ===
    DRAFT,        // El director crea el anteproyecto
    DRAFT_SUBMITTED,    // Enviado para revisión
    //
    JURY_ASSIGNED,           // Jurados asignados por el jefe de departamento
    FIRST_JURY_EVALUATION,
    //
    //
    DRAFT_REJECTED,     // Coordinador lo rechaza
    DRAFT_APPROVED,     // Coordinador aprueba el anteproyecto

    // === FASE FINAL ===
    COMPLETED,               // Sustentación realizada exitosamente
    FAILED;
}