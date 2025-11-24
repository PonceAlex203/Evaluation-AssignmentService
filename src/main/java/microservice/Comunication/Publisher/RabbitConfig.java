package microservice.Comunication.Publisher;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
/**
 * Configuration class for RabbitMQ queues and message converter
 */
public class RabbitConfig {

    @Value("${queue.creation}")
    private String creationQueue;

    @Value("${queue.modifier}")
    private String modifierQueue;

    @Value("${queue.notification}")
    private String notificationQueue;

    /**
     * Creates the creation queue
     * @return the queue instance
     */
    @Bean
    public Queue creationQueue() {
        return new Queue(creationQueue, true);
    }

    /**
     * Creates the modifier queue
     * @return the queue instance
     */
    @Bean
    public Queue modifierQueue() {
        return new Queue(modifierQueue, true);
    }

    /**
     * Creates the notification queue
     * @return the queue instance
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue, true);
    }

    /**
     * Configures JSON message converter
     * @return the message converter instance
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
