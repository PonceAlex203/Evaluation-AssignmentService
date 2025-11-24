package microservice.Comunication.Publisher;

import microservice.Comunication.Info.EvaluationEvent;
import microservice.Comunication.Info.NotificationEvent;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
/**
 * Handles message publishing to RabbitMQ queues
 */
public class Publisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.modifier}")
    private String modifierQueue;

    @Value("${queue.notification}")
    private String notificationQueue;

    /**
     * Constructs the publisher
     * @param rabbitTemplate the RabbitTemplate instance
     */
    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Sends evaluation event to modifier queue
     * @param pEvent the evaluation event to send
     */
    public void sendToModifierQueue(EvaluationEvent pEvent) {
        rabbitTemplate.convertAndSend(modifierQueue, pEvent);
    }

    /**
     * Sends notification event to notification queue
     * @param pMessage the notification event to send
     */
    public void sendToNotificationQueue(NotificationEvent pMessage) {
        rabbitTemplate.convertAndSend(notificationQueue, pMessage);
    }
}