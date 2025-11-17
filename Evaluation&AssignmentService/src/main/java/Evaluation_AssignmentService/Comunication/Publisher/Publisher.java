package Evaluation_AssignmentService.Comunication.Publisher;

import Evaluation_AssignmentService.Comunication.Info.EvaluationEvent;
import Evaluation_AssignmentService.ProcessEntity.BaseProcess;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class Publisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.modifier}")
    private String modifierQueue;

    @Value("${queue.notification}")
    private String notificationQueue;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToModifierQueue(EvaluationEvent pEvent) {
        rabbitTemplate.convertAndSend(modifierQueue, pEvent);
    }

    public void sendToNotificationQueue(String pMessage) {
        rabbitTemplate.convertAndSend(notificationQueue, pMessage);
    }

}