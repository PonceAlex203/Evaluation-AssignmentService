package Evaluation_AssignmentService.Comunication.Publisher;

import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.BaseProcess;

public final class MessageNotification {
    public static String ProcessUpdated(BaseProcess pUpdatedProcess){
        return String.format("===SE RESUBIO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pUpdatedProcess.getTypeProcess().getMessage());
    }
    public static String ProcessEvaluated(BaseProcess pEvaluatedProcess){
        return String.format("===SE EVALUO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pEvaluatedProcess.getTypeProcess().getMessage());
    }
    public static String ProcessSubmitted(BaseProcess pSavedProcess){
        return String.format("===SE SUBIO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pSavedProcess.getTypeProcess().getMessage());
    }
}

