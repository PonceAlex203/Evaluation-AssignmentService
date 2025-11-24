package microservice.Comunication.Publisher;

import microservice.ProcessEvaluation.Entities.Process.BaseProcess;

/**
 * Utility class for generating notification messages
 */
public final class MessageNotification{
    /**
     * Generates message for process update
     * @param pUpdatedProcess the updated process
     * @return formatted notification message
     */
    public static String processUpdated(BaseProcess pUpdatedProcess){
        return String.format("===SE RESUBIO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pUpdatedProcess.getTypeProcess().getMessage());
    }

    /**
     * Generates message for process evaluation
     * @param pEvaluatedProcess the evaluated process
     * @return formatted notification message
     */
    public static String processEvaluated(BaseProcess pEvaluatedProcess){
        return String.format("===SE EVALUO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pEvaluatedProcess.getTypeProcess().getMessage());
    }

    /**
     * Generates message for process submission
     * @param pSavedProcess the submitted process
     * @return formatted notification message
     */
    public static String processSubmitted(BaseProcess pSavedProcess){
        return String.format("===SE SUBIO UN PROCESO %s PERTENECIENTE AL TRABAJO DE GRADO.===",
                pSavedProcess.getTypeProcess().getMessage());
    }

    /**
     * Generates message for single evaluator assignment
     * @param pProcess the process with assignment
     * @return formatted notification message
     */
    public static String processAssigned(BaseProcess pProcess){
        return String.format("===HUBO UNA ASIGNACION DE 1 EVALUADOR AL TRABAJO DE GRADO===",
                pProcess.getTypeProcess().getMessage());
    }

    /**
     * Generates message for multiple evaluator assignments
     * @param pProcess the process with assignments
     * @return formatted notification message
     */
    public static String processAssigneds(BaseProcess pProcess){
        return String.format("===HUBO UNA ASIGNACIONES DE EVALUADORES AL TRABAJO DE GRADO===",
                pProcess.getTypeProcess().getMessage());
    }
}

