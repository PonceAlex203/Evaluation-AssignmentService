package Evaluation_AssignmentService.Comunication.Listener;

import Evaluation_AssignmentService.Comunication.Info.EvaluationEvent;
import Evaluation_AssignmentService.ProcessService.DegreeWorkService;
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
        System.out.println("===EVENTO RECIBIDO===");
        service.saveDegreeWorkId(event.getDegreeWorkId());
        System.out.println("GUARDADO ID: " +event.getDegreeWorkId() );
    }
}