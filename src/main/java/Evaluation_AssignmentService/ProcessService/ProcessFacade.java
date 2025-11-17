package Evaluation_AssignmentService.ProcessService;

import Evaluation_AssignmentService.Dto.EvaluateProcessDTO;
import Evaluation_AssignmentService.Dto.DraftDTO;
import Evaluation_AssignmentService.Dto.FormatADTO;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEntity.Presentation;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Facade class that centralizes access to different process services.
 * Provides unified operations for {@link Draft} and {@link FormatA}.
 */
@Service
public class ProcessFacade {
    @Autowired
    private ProcessFactory factory;
    @Autowired
    private DraftService draftService;
    @Autowired
    private FormatAService formatAService;
    @Autowired
    private PresentationService presentationService;
    @Autowired
    private DegreeWorkService degreeWorksService;

    public ProcessFacade() { }

    @Autowired
    public ProcessFacade(ProcessFactory factory, DraftService draftService, FormatAService formatAService, DegreeWorkService degreeWorksService) {
        this.factory = factory;
        this.draftService = draftService;
        this.formatAService = formatAService;
        this.degreeWorksService = degreeWorksService;
    }

    // ------------------- Draft methods -------------------

    /** Retrieves a draft by its degree work ID. */
    public Draft findDraftByDegreeWorkId(Long pId){ return draftService.findByDegreeWorkId(pId); }

    /** Creates and saves a new draft from a DTO. */
    public Draft saveDraft(DraftDTO pDto){
        degreeWorksService.validateExistingId(pDto.getDegreeWorkId());
        return draftService.save((Draft)factory.createProcessFromDTO(pDto));
    }

    /** Retrieves all drafts. */
    public List<Draft> getAllDrafts(){ return draftService.findAll(); }

    /** Reuploads an existing draft. */
    public Draft reUploadDraft(DraftDTO pDto){
        return draftService.reUploadProcess((Draft)factory.createProcessFromDTO(pDto));
    }

    /** Evaluates a draft process. */
    public Draft evaluateDraft(Long pId, EvaluateProcessDTO pDto){
        return draftService.evaluateProcess(pId, pDto.getComment(), pDto.getNewStatus());
    }

    /** Retrieves drafts by their status. */
    public List<Draft> getDraftsByStatus(EnumProcessStatus pStatus){
        return draftService.findByStatus(pStatus);
    }

    // ------------------- FormatA methods -------------------

    /** Retrieves a FormatA process by its degree work ID. */
    public FormatA getFormatAByDegreeWorkId(Long pId){ return formatAService.extractByDegreeWorkId(pId); }

    /** Creates and saves a new FormatA process from a DTO. */
    public FormatA saveFormatA(FormatADTO pDto){
        degreeWorksService.validateExistingId(pDto.getDegreeWorkId());
        return formatAService.save((FormatA) factory.createProcessFromDTO(pDto));
    }

    /** Retrieves all FormatA processes. */
    public List<FormatA> getAllFormatAs(){ return formatAService.findAll(); }

    /** Reuploads a FormatA process. */
    public FormatA reUploadFormatA(FormatADTO pDto){
        return formatAService.reUploadProcess((FormatA) factory.createProcessFromDTO(pDto));
    }

    /** Evaluates a FormatA process. */
    public FormatA evaluateFormatA(Long pId, EvaluateProcessDTO pDto){
        return formatAService.evaluateProcess(pId, pDto.getComment(), pDto.getNewStatus());
    }

    /** Retrieves all FormatA processes filtered by status. */
    public List<FormatA> getFormatsAByStatus(EnumProcessStatus pStatus) {
        return formatAService.findByStatus(pStatus);
    }

    // ------------------- Presentation methods -------------------
    public Presentation PresentationFindById(Long id){
        return presentationService.findById(id);
    }
    public Presentation Presentationsave(Presentation obj){
        return presentationService.save(obj);
    }
    public Presentation Presentationupdate(Long id, Presentation obj){ return presentationService.update(id, obj);}
}



