package Evaluation_AssignmentService.ProcessEvaluation.ProcessController;

import Evaluation_AssignmentService.ProcessEvaluation.Dto.DraftDTO;
import Evaluation_AssignmentService.ProcessEvaluation.Dto.EvaluateProcessDTO;
import Evaluation_AssignmentService.ProcessEvaluation.Dto.FormatADTO;
import Evaluation_AssignmentService.ProcessEvaluation.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessService.ProcessFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessFacade processFacade;

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
        return ResponseEntity.ok(draft);
    }
    @PutMapping("/draft/update")
    public ResponseEntity<Draft> reUploadDraft(@RequestBody DraftDTO pUpdatedDraft) {
        Draft vUploadDraft = processFacade.saveDraft(pUpdatedDraft);
        return ResponseEntity.ok(vUploadDraft);
    }
    @PutMapping("/draft/evaluate/{id}")
    public ResponseEntity<Draft> evaluateDraft(@PathVariable Long id, @RequestBody EvaluateProcessDTO request) {
        Draft vEvaluatedDraft = processFacade.evaluateDraft(id, request);
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
        return ResponseEntity.ok(vNewFormatA);
    }
    @PutMapping("/formatA/update")
    public ResponseEntity<FormatA> reUploadFormatA(@RequestBody FormatADTO pUpdatedFormatA) {
        FormatA vUploadFormatA = processFacade.reUploadFormatA(pUpdatedFormatA);
        return ResponseEntity.ok(vUploadFormatA);
    }
    @PutMapping("/formatA/evaluate/{id}")
    public ResponseEntity<FormatA> evaluateFormatA(@PathVariable Long id, @RequestBody EvaluateProcessDTO request) {
        FormatA vEvaluatedFormatA = processFacade.evaluateFormatA(id, request);
        return ResponseEntity.ok(vEvaluatedFormatA);
    }
    @GetMapping("/formatA/pending")
    public ResponseEntity<List<FormatA>> getPendingFormatsA() {
        return ResponseEntity.ok(processFacade.getFormatsAByStatus(EnumProcessStatus.PENDING));
    }

}