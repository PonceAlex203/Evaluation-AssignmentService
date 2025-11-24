package microservice.ProcessEvaluation.Services;

import microservice.ProcessEvaluation.Dtos.Input.AssignmentDTO;
import microservice.ProcessEvaluation.Dtos.Input.DoubleAssignmentDTO;
import microservice.ProcessEvaluation.Dtos.Input.EvaluationDTO;
import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Dtos.Mapper.ProcessMapperFactory;
import microservice.ProcessEvaluation.Dtos.Output.DraftResponseDTO;
import microservice.ProcessEvaluation.Dtos.Output.FormatAResponseDTO;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Interfaces.IMapper;
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
    private DegreeWorkService degreeWorksService;
    @Autowired
    private ProcessMapperFactory processMapperFactory;

    public ProcessFacade() {
    }

    @Autowired
    public ProcessFacade(ProcessFactory factory
            , DraftService draftService
            , FormatAService formatAService
            , DegreeWorkService degreeWorksService
            , ProcessMapperFactory processMapper) {
        this.factory = factory;
        this.draftService = draftService;
        this.formatAService = formatAService;
        this.degreeWorksService = degreeWorksService;
        this.processMapperFactory = processMapper;
    }

    // ------------------- Draft methods -------------------

    /**
     * Retrieves a draft by its degree work ID.
     */
    public Draft findDraftByDegreeWorkId(Long pId) {
        return draftService.findByDegreeWorkId(pId);
    }

    /**
     * Creates and saves a new draft from a DTO.
     */
    public DraftResponseDTO saveDraft(ProcessDTO pDto) {
        degreeWorksService.validateExistingId(pDto.getDegreeWorkId());
        Draft vNewDraft = draftService.save((Draft) factory.createProcessFromDTO(EnumTypeProcess.DRAFT,pDto));
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vMapper.toDto(vNewDraft);
    }
    /**
     * Retrieves all drafts.
     */
    public List<Draft> getAllDrafts() {
        return draftService.findAll();
    }

    /**
     * Reuploads an existing draft.
     */
    public DraftResponseDTO reUploadDraft(ProcessDTO pDto) {
        Draft vDraft = draftService.reUploadProcess((Draft) factory.createProcessFromDTO(EnumTypeProcess.DRAFT,pDto));
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vMapper.toDto(vDraft);
    }

    /**
     * Evaluates a draft process.
     */
    public DraftResponseDTO evaluateDraft(Long pId, EvaluationDTO pDto) {
        Draft vDraft = draftService.evaluateProcess(pId,pDto.getEvaluatorId(), pDto.getStatus() ,pDto.getComment());

        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);

        return vMapper.toDto(vDraft);
    }

    /**
     * Retrieves drafts by their status.
     */
    public List<DraftResponseDTO> getDraftsByStatus(EnumProcessStatus pStatus) {
        List<Draft> vDrafts = draftService.findByStatus(pStatus);

        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);

        return vDrafts.stream().map(vMapper::toDto).toList();
    }

    public DraftResponseDTO assignmentDraftEvaluator(AssignmentDTO pAssignment){
        Draft vDraft = draftService.assignmentEvaluator(pAssignment.getDegreeWorkId(), pAssignment.getEvaluatorId());
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);

        return vMapper.toDto(vDraft);
    }
    public DraftResponseDTO assignmentDraftEvaluators(DoubleAssignmentDTO pAssignment){
        Draft vDraft = draftService.assignedEvaluators(pAssignment.getDegreeWorkId()
                , pAssignment.getEvaluatorId1()
                , pAssignment.getEvaluatorId2());

        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);

        return vMapper.toDto(vDraft);
    }

    // ------------------- FormatA methods -------------------

    /**
     * Retrieves a FormatA process by its degree work ID.
     */
    public FormatAResponseDTO getFormatAByDegreeWorkId(Long pId) {
        FormatA vFormatA = formatAService.extractByDegreeWorkId(pId);
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Creates and saves a new FormatA process from a DTO.
     */
    public FormatAResponseDTO saveFormatA(ProcessDTO pDto) {
        degreeWorksService.validateExistingId(pDto.getDegreeWorkId());
        FormatA vFormatA = formatAService.save((FormatA) factory.createProcessFromDTO(EnumTypeProcess.FORMAT_A, pDto));
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Retrieves all FormatA processes.
     */
    public List<FormatA> getAllFormatAs() {
        return formatAService.findAll();
    }

    /**
     * Reuploads a FormatA process.
     */
    public FormatAResponseDTO reUploadFormatA(ProcessDTO pDto) {
        FormatA vFormatA = formatAService.reUploadProcess((FormatA) factory.createProcessFromDTO(EnumTypeProcess.FORMAT_A, pDto));
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Evaluates a FormatA process.
     */
    public FormatAResponseDTO evaluateFormatA(Long pId, EvaluationDTO pDto) {
        FormatA vFormatA = formatAService.evaluateProcess(pId, pDto.getEvaluatorId(), pDto.getStatus(),pDto.getComment());
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Retrieves all FormatA processes filtered by status.
     */
    public List<FormatAResponseDTO> getFormatsAByStatus(EnumProcessStatus pStatus) {
        List<FormatA> vFormatsA = formatAService.findByStatus(pStatus);
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);

        return vFormatsA.stream().map(vMapper::toDto).toList();
    }

}



