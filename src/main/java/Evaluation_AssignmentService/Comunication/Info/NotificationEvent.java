package Evaluation_AssignmentService.Comunication.Info;

import java.io.Serializable;

public class NotificationEvent implements Serializable {
    private final String message;
    private final Long degreeWorkId;


    public NotificationEvent(Long pDegreeWorkId, String pMessage) {
        this.message = pMessage;
        this.degreeWorkId = pDegreeWorkId;
    }

    public String getMessage() { return message;}
    public Long getDegreeWorkId() { return degreeWorkId;}
}