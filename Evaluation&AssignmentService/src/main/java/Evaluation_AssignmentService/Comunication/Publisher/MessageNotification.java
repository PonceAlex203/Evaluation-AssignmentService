package Evaluation_AssignmentService.Comunication.Publisher;

import Evaluation_AssignmentService.ProcessEntity.BaseProcess;

public final class MessageNotification {
    public static String ProcessUpdated(BaseProcess pUpdatedProcess){
        return String.format("===SE RESUBIO UN PROCESO ",
                pUpdatedProcess.getTypeProcess(),
                " PERTENECIENTE AL TRABAJO DE GRADO CON ID: " + pUpdatedProcess.getDegreeworkId(),
                "===");
    }
    public static String ProcessEvaluated(BaseProcess pEvaluatedProcess){
        return String.format("===SE EVALUO UN PROCESO ",
                pEvaluatedProcess.getTypeProcess(),
                " PERTENECIENTE AL TRABAJO DE GRADO CON ID: " + pEvaluatedProcess.getDegreeworkId(),
                "===");
    }
}
