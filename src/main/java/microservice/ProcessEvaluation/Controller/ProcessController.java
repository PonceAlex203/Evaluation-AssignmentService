package microservice.ProcessEvaluation.Controller;

import microservice.ProcessEvaluation.Dtos.Input.AssignmentDTO;
import microservice.ProcessEvaluation.Dtos.Input.DoubleAssignmentDTO;
import microservice.ProcessEvaluation.Dtos.Input.EvaluationDTO;
import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Dtos.Output.DraftResponseDTO;
import microservice.ProcessEvaluation.Dtos.Output.FormatAResponseDTO;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Services.ProcessFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessFacade processFacade;

    @Autowired
    public ProcessController(ProcessFacade processFacade) {
        this.processFacade = processFacade;
    }

    //FormatA
    @GetMapping("/formatA/{id}")
    public ResponseEntity<FormatAResponseDTO> getFormatAById(@PathVariable Long id) {
        return ResponseEntity.ok(processFacade.getFormatAByDegreeWorkId(id));
    }
    @GetMapping("/formatA/all")
    public ResponseEntity<List<FormatA>> getAllFormatAs() {
        return ResponseEntity.ok(processFacade.getAllFormatAs());
    }
    @PostMapping("/formatA")
    public ResponseEntity<FormatAResponseDTO> saveFormatA(@RequestBody ProcessDTO pFormatA) {
        FormatAResponseDTO vNewFormatA = processFacade.saveFormatA(pFormatA);
        return ResponseEntity.ok(vNewFormatA);
    }
    @PutMapping("/formatA/update")
    public ResponseEntity<FormatAResponseDTO> reUploadFormatA(@RequestBody ProcessDTO pUpdatedFormatA) {
        FormatAResponseDTO vUploadFormatA = processFacade.reUploadFormatA(pUpdatedFormatA);
        return ResponseEntity.ok(vUploadFormatA);
    }
    @PutMapping("/formatA/evaluate")
    public ResponseEntity<FormatAResponseDTO> evaluateFormatA(@RequestBody EvaluationDTO request) {
        FormatAResponseDTO vEvaluatedFormatA = processFacade.evaluateFormatA(request.getDegreeWorkId(), request);
        return ResponseEntity.ok(vEvaluatedFormatA);
    }
    @GetMapping("/formatA/pending")
    public ResponseEntity<List<FormatAResponseDTO>> getPendingFormatsA() {
        return ResponseEntity.ok(processFacade.getFormatsAByStatus(EnumProcessStatus.PENDING));
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
    public ResponseEntity<DraftResponseDTO> saveDraft(@RequestBody ProcessDTO pDraft) {
        DraftResponseDTO draft = processFacade.saveDraft(pDraft);
        return ResponseEntity.ok(draft);
    }
    @PutMapping("/draft/update")
    public ResponseEntity<DraftResponseDTO> reUploadDraft(@RequestBody ProcessDTO pUpdatedDraft) {
        DraftResponseDTO vUploadDraft = processFacade.saveDraft(pUpdatedDraft);
        return ResponseEntity.ok(vUploadDraft);
    }
    @PutMapping("/draft/evaluate")
    public ResponseEntity<DraftResponseDTO> evaluateDraft(@RequestBody EvaluationDTO request) {
        DraftResponseDTO vEvaluatedDraft = processFacade.evaluateDraft(request.getDegreeWorkId(), request);
        return ResponseEntity.ok(vEvaluatedDraft);
    }
    @PutMapping("/draft/Assignment")
    public ResponseEntity<DraftResponseDTO> assignmentEvaluator(@RequestBody AssignmentDTO request){
        DraftResponseDTO vDraft = processFacade.assignmentDraftEvaluator(request);
        return ResponseEntity.ok(vDraft);
    }
    @PutMapping("/draft/Assignments")
    public ResponseEntity<DraftResponseDTO> assignmentEvaluators(@RequestBody DoubleAssignmentDTO request){
        DraftResponseDTO vDraft = processFacade.assignmentDraftEvaluators(request);
        return ResponseEntity.ok(vDraft);
    }
    @GetMapping("/draft/pending")
    public ResponseEntity<List<DraftResponseDTO>> getPendingDrafts() {
        List<DraftResponseDTO> vListPending = processFacade.getDraftsByStatus(EnumProcessStatus.PENDING);
        List<DraftResponseDTO> vListPartial = processFacade.getDraftsByStatus(EnumProcessStatus.PARTIAL);
        List<DraftResponseDTO> vListAssigned = processFacade.getDraftsByStatus(EnumProcessStatus.ASSIGNED);

        List<DraftResponseDTO> vCombined = new ArrayList<>();
        vCombined.addAll(vListPending);
        vCombined.addAll(vListPartial);
        vCombined.addAll(vListAssigned);

        return ResponseEntity.ok(vCombined);
    }
}