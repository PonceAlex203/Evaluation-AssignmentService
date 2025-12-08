package microservice.ProcessEvaluation.Entities.Factories;

import microservice.ProcessEvaluation.Entities.Builders.DraftBuilder;
import microservice.ProcessEvaluation.Entities.Builders.FormatABuilder;
import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Interfaces.IProcessBuilder;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import microservice.ProcessEvaluation.Interfaces.IProcessFactory;
import microservice.SecurityComponent.ProcessException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * Factory class responsible for creating process instances based on their type.
 * Supports creation using DTOs as well as simple creation with only a degree work ID.
 */
@Component
public class ProcessFactory implements IProcessFactory {

    private final Map<EnumTypeProcess, IProcessBuilder<?, ?>> buildersByType;
    private final Map<EnumTypeProcess, Function<Long, BaseProcess>> simpleCreators;

    /**
     * Initializes the factory with its available builders and simple creators.
     *
     * @param formatABuilder the builder for FormatA processes
     * @param draftBuilder the builder for Draft processes
     */
    public ProcessFactory(
            FormatABuilder formatABuilder,
            DraftBuilder draftBuilder
    ) {
        this.buildersByType = Map.of(
                EnumTypeProcess.FORMAT_A, formatABuilder,
                EnumTypeProcess.DRAFT, draftBuilder
        );

        this.simpleCreators = Map.of(
                EnumTypeProcess.FORMAT_A, degreeWorkId -> new FormatA(new CoreProcess(degreeWorkId), null),
                EnumTypeProcess.DRAFT, degreeWorkId -> new Draft(new CoreProcess(degreeWorkId), null)
        );
    }

    /**
     * Creates a process using its type and a DTO.
     *
     * @param pType the process type
     * @param pDto the process information DTO
     * @return the constructed process
     * @throws ProcessException if the type is not supported
     */
    @Override
    public BaseProcess createProcessFromDTO(EnumTypeProcess pType, ProcessDTO pDto) {
        IProcessBuilder builder = buildersByType.get(pType);
        if (builder == null) {
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_SUPPORTED);
        }
        return buildProcessFromDTO(builder, pDto);
    }

    /**
     * Internal helper to build a process using a generic builder.
     *
     * @param builder the builder associated with the process type
     * @param dto the process information DTO
     * @param <T> the resulting process type
     * @param <D> the DTO type
     * @return the constructed process
     */
    private <T extends BaseProcess, D extends ProcessDTO> T buildProcessFromDTO(
            IProcessBuilder<T, D> builder,
            ProcessDTO dto
    ) {
        return builder.buildFromDTO((D) dto);
    }

    /**
     * Creates a process instance using only its type and degree work identifier.
     *
     * @param pTypeProcess the process type
     * @param pDegreeWorkId the degree work identifier
     * @return a new process instance
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


