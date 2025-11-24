package microservice.Comunication.Info;

import java.io.Serializable;

/**
 * Represents a notification event in the system
 */
public class NotificationEvent implements Serializable {
    private final String message;
    private final Long degreeWorkId;

    /**
     * Constructs a notification event
     * @param pDegreeWorkId the degree work identifier
     * @param pMessage the notification message
     */
    public NotificationEvent(Long pDegreeWorkId, String pMessage) {
        this.message = pMessage;
        this.degreeWorkId = pDegreeWorkId;
    }

    /**
     * @return the notification message
     */
    public String getMessage() { return message;}

    /**
     * @return the degree work identifier
     */
    public Long getDegreeWorkId() { return degreeWorkId;}
}