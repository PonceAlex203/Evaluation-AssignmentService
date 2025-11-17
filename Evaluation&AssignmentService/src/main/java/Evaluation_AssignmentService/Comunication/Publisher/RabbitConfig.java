package Evaluation_AssignmentService.Comunication.Publisher;

//import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Value("${queue.creation}")
    private String creationQueue;

    @Value("${queue.modifier}")
    private String modifierQueue;

    @Value("${queue.notification}")
    private String notificationQueue;

    @Bean
    public Queue creationQueue() {
        return new Queue(creationQueue, true);
    }
    @Bean
    public Queue modifierQueue() {
        return new Queue(modifierQueue, true);
    }
    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue, true);
    }
}
