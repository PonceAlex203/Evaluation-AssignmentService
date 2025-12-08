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
 * Facade service for managing degree work processes, providing high-level
 * operations for Draft and Format A workflows.
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

    /**
     * Default constructor.
     */
    public ProcessFacade() {}

    /**
     * Constructs the facade with required services.
     *
     * @param factory the process factory
     * @param draftService the draft service
     * @param formatAService the Format A service
     * @param degreeWorksService the degree work service
     * @param processMapper the mapper factory
     */
    @Autowired
    public ProcessFacade(ProcessFactory factory,
                         DraftService draftService,
                         FormatAService formatAService,
                         DegreeWorkService degreeWorksService,
                         ProcessMapperFactory processMapper) {
        this.factory = factory;
        this.draftService = draftService;
        this.formatAService = formatAService;
        this.degreeWorksService = degreeWorksService;
        this.processMapperFactory = processMapper;
    }

    // ------------------- Draft methods -------------------

    /**
     * Retrieves a draft process associated with a degree work ID.
     *
     * @param pId the degree work ID
     * @return the draft process
     */
    public Draft findDraftByDegreeWorkId(Long pId) {
        return draftService.searchBy(pId);
    }

    /**
     * Creates a new draft process.
     *
     * @param pDto the process data
     * @return the created draft as a DTO
     */
    public DraftResponseDTO saveDraft(ProcessDTO pDto) {
        Draft vNewDraft = draftService.save((Draft) factory.createProcessFromDTO(EnumTypeProcess.DRAFT, pDto));
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vMapper.toDto(vNewDraft);
    }

    /**
     * Retrieves all draft processes.
     *
     * @return list of drafts
     */
    public List<Draft> getAllDrafts() {
        return draftService.getAll();
    }

    /**
     * Evaluates an existing draft process.
     *
     * @param pEvaluatorId evaluator identifier
     * @param pDto evaluation data
     * @param pNewStatus new evaluation status
     * @return updated draft as DTO
     */
    public DraftResponseDTO evaluateDraft(Long pEvaluatorId, EvaluationDTO pDto, EnumProcessStatus pNewStatus) {
        Draft vDraft = draftService.evaluateProcess(pDto.getDegreeWorkId(), pEvaluatorId, pNewStatus, pDto.getComment());
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vMapper.toDto(vDraft);
    }

    /**
     * Assigns an evaluator to a draft.
     *
     * @param pAssignment assignment data
     * @return updated draft as DTO
     */
    public DraftResponseDTO assignmentDraftEvaluator(AssignmentDTO pAssignment) {
        Draft vDraft = draftService.assignmentEvaluator(pAssignment.getDegreeWorkId(), pAssignment.getEvaluatorId());
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vMapper.toDto(vDraft);
    }

    /**
     * Assigns two evaluators to a draft.
     *
     * @param pAssignment assignment data
     * @return updated draft as DTO
     */
    public DraftResponseDTO assignmentDraftEvaluators(DoubleAssignmentDTO pAssignment) {
        Draft vDraft = draftService.assignedEvaluators(pAssignment.getDegreeWorkId(),
                pAssignment.getEvaluatorId1(),
                pAssignment.getEvaluatorId2());

        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vMapper.toDto(vDraft);
    }

    /**
     * Retrieves drafts awaiting evaluator assignment.
     *
     * @return list of pending assignment drafts as DTOs
     */
    public List<DraftResponseDTO> getPendingAssignedDrafts() {
        List<Draft> vDrafts = draftService.getPendingAssignment();
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vDrafts.stream().map(vMapper::toDto).toList();
    }

    /**
     * Retrieves drafts pending evaluation for a given evaluator.
     *
     * @param pIdEvaluator evaluator identifier
     * @return list of draft DTOs
     */
    public List<DraftResponseDTO> getPendingEvaluateDrafts(Long pIdEvaluator) {
        List<Draft> vDrafts = draftService.getPendingEvaluate(pIdEvaluator);
        IMapper<Draft, DraftResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.DRAFT);
        return vDrafts.stream().map(vMapper::toDto).toList();
    }

    // ------------------- FormatA methods -------------------

    /**
     * Retrieves a Format A process by degree work ID.
     *
     * @param pId the degree work ID
     * @return Format A DTO
     */
    public FormatAResponseDTO getFormatAByDegreeWorkId(Long pId) {
        FormatA vFormatA = formatAService.getBy(pId);
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Creates a new Format A process.
     *
     * @param pDto the process data
     * @return created Format A as DTO
     */
    public FormatAResponseDTO saveFormatA(ProcessDTO pDto) {
        degreeWorksService.validateExistingId(pDto.getDegreeWorkId());
        FormatA vFormatA = formatAService.save((FormatA) factory.createProcessFromDTO(EnumTypeProcess.FORMAT_A, pDto));
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Retrieves all Format A processes.
     *
     * @return list of Format A processes
     */
    public List<FormatA> getAllFormatAs() {
        return formatAService.getAll();
    }

    /**
     * Reuploads a previously rejected Format A process.
     *
     * @param pDto process data
     * @return updated Format A as DTO
     */
    public FormatAResponseDTO reUploadFormatA(ProcessDTO pDto) {
        FormatA vFormatA = formatAService.reUploadProcess((FormatA) factory.createProcessFromDTO(EnumTypeProcess.FORMAT_A, pDto));
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Evaluates a Format A process.
     *
     * @param pEvaluatorId evaluator identifier
     * @param pDto evaluation data
     * @param pNewStatus new evaluation status
     * @return evaluated Format A as DTO
     */
    public FormatAResponseDTO evaluateFormatA(Long pEvaluatorId, EvaluationDTO pDto, EnumProcessStatus pNewStatus) {
        FormatA vFormatA = formatAService.evaluateProcess(pDto.getDegreeWorkId(), pEvaluatorId, pNewStatus, pDto.getComment());
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vMapper.toDto(vFormatA);
    }

    /**
     * Retrieves Format A processes pending evaluation.
     *
     * @return list of pending Format A DTOs
     */
    public List<FormatAResponseDTO> getPendingFormatsA() {
        List<FormatA> vFormatsA = formatAService.getPendingEvaluations();
        IMapper<FormatA, FormatAResponseDTO> vMapper =
                processMapperFactory.getMapper(EnumTypeProcess.FORMAT_A);
        return vFormatsA.stream().map(vMapper::toDto).toList();
    }
}



