package Evaluation_AssignmentService.ProcessEntity;

import Evaluation_AssignmentService.Builders.DraftBuilder;
import Evaluation_AssignmentService.Builders.FormatABuilder;
import Evaluation_AssignmentService.Dto.DraftDTO;
import Evaluation_AssignmentService.Dto.FormatADTO;
import Evaluation_AssignmentService.Dto.ProcessDTO;
import Evaluation_AssignmentService.Interface.IProcessBuilder;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.Enum.EnumTypeProcess;
import Evaluation_AssignmentService.Interface.IProcessFactory;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory implementation for creating process entities.
 * Uses registered builders and creators based on process type or DTO.
 */
@Component
public class ProcessFactory implements IProcessFactory {

    /** Maps DTO types to their respective builders. */
    private final Map<Class<? extends ProcessDTO>, IProcessBuilder<?, ?>> buildersByDTOType;

    /** Maps process types to simple constructors. */
    private final Map<EnumTypeProcess, Function<Long, BaseProcess>> simpleCreators;

    /**
     * Constructor registering builders and creators for each process type.
     * @param formatABuilder builder for FormatA
     * @param draftBuilder builder for Draft
     */
    public ProcessFactory(FormatABuilder formatABuilder, DraftBuilder draftBuilder) {
        buildersByDTOType = new HashMap<>();
        buildersByDTOType.put(FormatADTO.class, formatABuilder);
        buildersByDTOType.put(DraftDTO.class, draftBuilder);

        simpleCreators = new HashMap<>();
        simpleCreators.put(EnumTypeProcess.FORMAT_A, FormatA::new);
        simpleCreators.put(EnumTypeProcess.DRAFT, Draft::new);
    }

    /**
     * Creates a process entity from a given DTO using its mapped builder.
     * @param dto DTO containing process data
     * @return created process entity
     * @throws ProcessException if no builder supports the given DTO type
     */
    @Override
    public BaseProcess createProcessFromDTO(ProcessDTO dto) {
        IProcessBuilder builder = buildersByDTOType.get(dto.getClass());
        if (builder == null) {
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_SUPPORTED);
        }
        return buildProcessFromDTO(builder, dto);
    }

    /**
     * Internal helper to safely build a process from a DTO using a generic builder.
     */
    private <T extends BaseProcess, D extends ProcessDTO> T buildProcessFromDTO(
            IProcessBuilder<T, D> builder, ProcessDTO dto) {
        return builder.buildFromDTO((D) dto);
    }

    /**
     * Creates a process entity by type and degree work ID.
     * @param pTypeProcess type of process to create
     * @param pDegreeWorkId degree work ID
     * @return created process entity
     * @throws ProcessException if the type is not supported
     */
    @Override
    public BaseProcess createProcess(EnumTypeProcess pTypeProcess, Long pDegreeWorkId) {
        Function<Long, BaseProcess> vCreator = simpleCreators.get(pTypeProcess);
        if (vCreator == null) {
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_SUPPORTED);
        }
        return vCreator.apply(pDegreeWorkId);
    }
}

