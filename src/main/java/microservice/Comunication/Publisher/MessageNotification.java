package microservice.Comunication.Publisher;

import microservice.ProcessEvaluation.Entities.Process.BaseProcess;

public final class MessageNotification{
    public static String processUpdated(BaseProcess pUpdatedProcess){
        return String.format("===SE RESUBIO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pUpdatedProcess.getTypeProcess().getMessage());
    }
    public static String processEvaluated(BaseProcess pEvaluatedProcess){
        return String.format("===SE EVALUO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pEvaluatedProcess.getTypeProcess().getMessage());
    }
    public static String processSubmitted(BaseProcess pSavedProcess){
        return String.format("===SE SUBIO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pSavedProcess.getTypeProcess().getMessage());
    }
    public static String processAssigned(BaseProcess pProcess){
        return String.format("===HUBO UNA ASIGNACION DE 1 EVALUADOR AL TRABAJO DE GRADO===",
                pProcess.getTypeProcess().getMessage());
    }
    public static String processAssigneds(BaseProcess pProcess){
        return String.format("===HUBO UNA ASIGNACIONES DE EVALUADORES AL TRABAJO DE GRADO===",
                pProcess.getTypeProcess().getMessage());
    }
}

