package microservice.Comunication.Listener;

import microservice.Comunication.Info.EvaluationEvent;
import microservice.ProcessEvaluation.Services.DegreeWorkService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DegreeWorkEventListener {

    private final DegreeWorkService service;

    public DegreeWorkEventListener(DegreeWorkService pService) {
        this.service = pService;
    }

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