package microservice.Comunication.Listener;

import microservice.Comunication.Info.EvaluationEvent;
import microservice.ProcessEvaluation.Services.DegreeWorkService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
/**
 * Handles degree work related events from message queue
 */
public class DegreeWorkEventListener {

    private final DegreeWorkService service;

    /**
     * Constructs the event listener
     * @param pService the degree work service
     */
    public DegreeWorkEventListener(DegreeWorkService pService) {
        this.service = pService;
    }

    /**
     * Processes degree work evaluation events from the queue
     * @param event the evaluation event to process
     */
    @RabbitListener(queues = "${queue.creation}")
    public void handlerDegreeWorkEvent(EvaluationEvent event) {
        try{
            System.out.println("===EVENTO RECIBIDO===");
            service.saveDegreeWorkId(event.getDegreeWorkId());
            System.out.println("GUARDADO ID: " +event.getDegreeWorkId() );
        }catch (Exception vExc){
        }
    }
}