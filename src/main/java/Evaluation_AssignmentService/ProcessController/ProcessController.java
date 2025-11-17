package Evaluation_AssignmentService.ProcessController;

import Evaluation_AssignmentService.Comunication.Info.EnumDegreeWorkStateType;
import Evaluation_AssignmentService.Comunication.Info.EvaluationEvent;
import Evaluation_AssignmentService.Comunication.Publisher.MessageNotification;
import Evaluation_AssignmentService.Dto.*;
import Evaluation_AssignmentService.Comunication.Publisher.Publisher;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEntity.Presentation;
import Evaluation_AssignmentService.ProcessService.ProcessFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessFacade processFacade;
    @Autowired
    private Publisher publisher;

    @Autowired
    public ProcessController(ProcessFacade processFacade) {
        this.processFacade = processFacade;
    }
    //Draft
    @GetMapping("/draft/{id}")
    public ResponseEntity<Draft> getDraftById(@PathVariable Long id) {
        return ResponseEntity.ok(processFacade.findDraftByDegreeWorkId(id));
    }
    @GetMapping("/draft/all")
    public ResponseEntity<List<Draft>> getAllDrafts() {
        return ResponseEntity.ok(processFacade.getAllDrafts());
    }
    @PostMapping("/draft")
    public ResponseEntity<Draft> saveDraft(@RequestBody DraftDTO pDraft) {
        Draft draft = processFacade.saveDraft(pDraft);
        publisher.sendToModifierQueue(new EvaluationEvent(draft.getDegreeworkId(), EnumDegreeWorkStateType.DRAFT_SUBMITTED));
        return ResponseEntity.ok(draft);
    }
    @PutMapping("/draft/update")
    public ResponseEntity<Draft> reUploadDraft(@RequestBody DraftDTO pUpdatedDraft) {
        Draft vUploadDraft = processFacade.saveDraft(pUpdatedDraft);
        publisher.sendToNotificationQueue(MessageNotification.ProcessUpdated(vUploadDraft));
        return ResponseEntity.ok(vUploadDraft);
    }
    @PutMapping("/draft/evaluate/{id}")
    public ResponseEntity<Draft> evaluateDraft(@PathVariable Long id, @RequestBody EvaluateProcessDTO request) {
        Draft vEvaluatedDraft = processFacade.evaluateDraft(id, request);
        publisher.sendToNotificationQueue(MessageNotification.ProcessEvaluated(vEvaluatedDraft));
        return ResponseEntity.ok(vEvaluatedDraft);
    }
    @GetMapping("/draft/pending")
    public ResponseEntity<List<Draft>> getPendingDrafts() {
        return ResponseEntity.ok(processFacade.getDraftsByStatus(EnumProcessStatus.PENDING));
    }

    //FormatA
    @GetMapping("/formatA/{id}")
    public ResponseEntity<FormatA> getFormatAById(@PathVariable Long id) {
        return ResponseEntity.ok(processFacade.getFormatAByDegreeWorkId(id));
    }
    @GetMapping("/formatA/all")
    public ResponseEntity<List<FormatA>> getAllFormatAs() {
        return ResponseEntity.ok(processFacade.getAllFormatAs());
    }
    @PostMapping("/formatA")
    public ResponseEntity<FormatA> saveFormatA(@RequestBody FormatADTO pFormatA) {
        FormatA vNewFormatA = processFacade.saveFormatA(pFormatA);
        publisher.sendToModifierQueue(new EvaluationEvent(pFormatA.getDegreeWorkId(), EnumDegreeWorkStateType.FORMAT_A_SUBMITTED));
        return ResponseEntity.ok(vNewFormatA);
    }
    @PutMapping("/formatA/update")
    public ResponseEntity<FormatA> reUploadFormatA(@RequestBody FormatADTO pUpdatedFormatA) {
        FormatA vUploadFormatA = processFacade.reUploadFormatA(pUpdatedFormatA);
        publisher.sendToNotificationQueue(MessageNotification.ProcessUpdated(vUploadFormatA));
        return ResponseEntity.ok(vUploadFormatA);
    }
    @PutMapping("/formatA/evaluate/{id}")
    public ResponseEntity<FormatA> evaluateFormatA(@PathVariable Long id, @RequestBody EvaluateProcessDTO request) {
        FormatA vEvaluatedFormatA = processFacade.evaluateFormatA(id, request);
        publisher.sendToNotificationQueue(MessageNotification.ProcessEvaluated(vEvaluatedFormatA));
        return ResponseEntity.ok(vEvaluatedFormatA);
    }
    @GetMapping("/formatA/pending")
    public ResponseEntity<List<FormatA>> getPendingFormatsA() {
        return ResponseEntity.ok(processFacade.getFormatsAByStatus(EnumProcessStatus.PENDING));
    }

    //Presentation
    @GetMapping("/presentation/{id}")
    public ResponseEntity<Presentation> getPresentationById(@PathVariable Long id) {
        return ResponseEntity.ok(processFacade.PresentationFindById(id));
    }
    @PostMapping("/Presentation")
    public ResponseEntity<Presentation> savePresentation(@RequestBody PresentationDTO pPresentation) {
        return ResponseEntity.ok(processFacade.Presentationsave(new Presentation(pPresentation.getIdDegreeWork(),pPresentation.getIdjurys())));
    }
    @PutMapping("/Presentation/update/{id}")
    public ResponseEntity<Presentation> reUploadPresentation(@PathVariable Long id, @RequestBody PresentationDTO pPresentation) {
        return ResponseEntity.ok(processFacade.Presentationupdate(id,new Presentation(pPresentation.getIdDegreeWork(),pPresentation.getIdjurys())));
    }
}