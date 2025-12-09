package microservice.ProcessEvaluation.Controller;

import microservice.ProcessEvaluation.Dtos.Input.AssignmentDTO;
import microservice.ProcessEvaluation.Dtos.Input.DoubleAssignmentDTO;
import microservice.ProcessEvaluation.Dtos.Input.EvaluationDTO;
import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Dtos.Output.DraftResponseDTO;
import microservice.ProcessEvaluation.Dtos.Output.FormatAResponseDTO;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Services.ProcessFacade;
import microservice.SecurityComponent.config.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
@RestController
@RequestMapping("/process")
/**
 * REST controller for managing degree work processes
 */
public class ProcessController {

    private final ProcessFacade processFacade;

    @Autowired
    public ProcessController(ProcessFacade processFacade) {
        this.processFacade = processFacade;
    }

    //FormatA
    /**
     * Retrieves Format A by degree work ID
     * @param id the degree work identifier
     * @return Format A response
     */
    @GetMapping("/formatA/{id}")
    public ResponseEntity<FormatAResponseDTO> getFormatAById(@PathVariable Long id) {
        return ResponseEntity.ok(processFacade.getFormatAByDegreeWorkId(id));
    }

    /**
     * Retrieves all Format A documents
     * @return list of Format A documents
     */
    @GetMapping("/formatA/all")
    public ResponseEntity<List<FormatA>> getAllFormatAs() {
        return ResponseEntity.ok(processFacade.getAllFormatAs());
    }

    /**
     * Creates a new Format A document
     * @param pFormatA the Format A data
     * @return created Format A response
     */
    @PostMapping("/formatA")
    public ResponseEntity<FormatAResponseDTO> saveFormatA(@RequestBody ProcessDTO pFormatA) {
        FormatAResponseDTO vNewFormatA = processFacade.saveFormatA(pFormatA);
        return ResponseEntity.ok(vNewFormatA);
    }

    /**
     * Updates an existing Format A document
     * @param pUpdatedFormatA the updated Format A data
     * @return updated Format A response
     */
    @PutMapping("/formatA/update")
    public ResponseEntity<FormatAResponseDTO> reUploadFormatA(@RequestBody ProcessDTO pUpdatedFormatA) {
        FormatAResponseDTO vUploadFormatA = processFacade.reUploadFormatA(pUpdatedFormatA);
        return ResponseEntity.ok(vUploadFormatA);
    }

    /**
     * Retrieves pending Format A documents
     * @return list of pending Format A documents
     */
    @GetMapping("/formatA/pending")
    public ResponseEntity<List<FormatAResponseDTO>> getPendingFormatsA() {
        return ResponseEntity.ok(processFacade.getPendingFormatsA());
    }

    /**
     * Approves a Format A document
     * @param request the evaluation data
     * @return approved Format A response
     */
    @PutMapping("/formatA/evaluate/approve")
    public ResponseEntity<FormatAResponseDTO> approveFormatA(@RequestBody EvaluationDTO request) {
        FormatAResponseDTO vEvaluatedFormatA = processFacade.evaluateFormatA(JwtRequestFilter.getCurrentAccountId(), request,EnumProcessStatus.APPROVED);
        return ResponseEntity.ok(vEvaluatedFormatA);                        //Enviar id del Jwt
    }

    /**
     * Rejects a Format A document
     * @param request the evaluation data
     * @return rejected Format A response
     */
    @PutMapping("/formatA/evaluate/reject")
    public ResponseEntity<FormatAResponseDTO> rejectFormatA(@RequestBody EvaluationDTO request) {
        FormatAResponseDTO vEvaluatedFormatA = processFacade.evaluateFormatA(JwtRequestFilter.getCurrentAccountId(), request,EnumProcessStatus.REJECTED);
        return ResponseEntity.ok(vEvaluatedFormatA);                        //Enviar id del Jwt
    }

    //Draft
    /**
     * Retrieves draft by degree work ID
     * @param id the degree work identifier
     * @return draft document
     */
    @GetMapping("/draft/{id}")
    public ResponseEntity<Draft> getDraftById(@PathVariable Long id) {
        return ResponseEntity.ok(processFacade.findDraftByDegreeWorkId(id));
    }

    /**
     * Retrieves all draft documents
     * @return list of draft documents
     */
    @GetMapping("/draft/all")
    public ResponseEntity<List<Draft>> getAllDrafts() {
        return ResponseEntity.ok(processFacade.getAllDrafts());
    }


    /**
     * Creates a new draft document
     * @param pDraft the draft data
     * @return created draft response
     */
    @PostMapping("/draft")
    public ResponseEntity<DraftResponseDTO> saveDraft(@RequestBody ProcessDTO pDraft) {
        DraftResponseDTO draft = processFacade.saveDraft(pDraft);
        return ResponseEntity.ok(draft);
    }

    /**
     * Approves a draft document
     * @param request the evaluation data
     * @return approved draft response
     */
    @PutMapping("/draft/evaluate/approve")
    public ResponseEntity<DraftResponseDTO> approveDraft(@RequestBody EvaluationDTO request) {
        DraftResponseDTO vEvaluatedDraft = processFacade.evaluateDraft(JwtRequestFilter.getCurrentAccountId(),request, EnumProcessStatus.APPROVED);
        return ResponseEntity.ok(vEvaluatedDraft);                      //Enviar id del Jwt
    }

    /**
     * Rejects a draft document
     * @param request the evaluation data
     * @return rejected draft response
     */
    @PutMapping("/draft/evaluate/reject")
    public ResponseEntity<DraftResponseDTO> rejectDraft(@RequestBody EvaluationDTO request) {
        DraftResponseDTO vEvaluatedDraft = processFacade.evaluateDraft(JwtRequestFilter.getCurrentAccountId(),request, EnumProcessStatus.REJECTED);
        return ResponseEntity.ok(vEvaluatedDraft);
    }

    /**
     * Assigns a single evaluator to a draft
     * @param request the assignment data
     * @return updated draft response
     */
    @PutMapping("/draft/Assignment")
    public ResponseEntity<DraftResponseDTO> assignmentEvaluator(@RequestBody AssignmentDTO request){
        DraftResponseDTO vDraft = processFacade.assignmentDraftEvaluator(request);
        return ResponseEntity.ok(vDraft);
    }

    /**
     * Assigns multiple evaluators to a draft
     * @param request the multiple assignment data
     * @return updated draft response
     */
    @PutMapping("/draft/Assignments")
    public ResponseEntity<DraftResponseDTO> assignmentEvaluators(@RequestBody DoubleAssignmentDTO request){
        DraftResponseDTO vDraft = processFacade.assignmentDraftEvaluators(request);
        return ResponseEntity.ok(vDraft);
    }

    /**
     * Retrieves drafts in pending, partial, and assigned generalEvaluationStatus
     * @return list of drafts in pending status
     */
    @GetMapping("/draft/evaluate/pending")
    public ResponseEntity<List<DraftResponseDTO>> getPendingEvaluateDraftsByEvluatorId() {
        List<DraftResponseDTO> vListPending = processFacade.getPendingEvaluateDrafts(JwtRequestFilter.getCurrentAccountId());//Enviar id del Jwt
        return ResponseEntity.ok(vListPending);
    }

    /**
     * Retrieves pending drafts to assign by the department head
     * @return list of pending drafts to be evaluated according to the requester's id
     */
    @GetMapping("/draft/assign/pending")
    public ResponseEntity<List<DraftResponseDTO>> getPendingAssignedDraftsByDeptId() {
        List<DraftResponseDTO> vListPending = processFacade.getPendingAssignedDrafts();
        return ResponseEntity.ok(vListPending);
    }

}