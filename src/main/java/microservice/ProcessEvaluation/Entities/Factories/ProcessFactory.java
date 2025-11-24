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
 * Factory implementation for creating process entities.
 * Uses registered builders and creators based on process type or DTO.
 */
@Component
public class ProcessFactory implements IProcessFactory {

    private final Map<EnumTypeProcess, IProcessBuilder<?, ?>> buildersByType;
    private final Map<EnumTypeProcess, Function<Long, BaseProcess>> simpleCreators;

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
                EnumTypeProcess.DRAFT, degreeWorkId ->new Draft(new CoreProcess(degreeWorkId), null)
        );
    }

    @Override
    public BaseProcess createProcessFromDTO(EnumTypeProcess pType, ProcessDTO pDto) {
        IProcessBuilder builder = buildersByType.get(pType);
        if (builder == null) {
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_SUPPORTED);
        }
        return buildProcessFromDTO(builder, pDto);
    }

    private <T extends BaseProcess, D extends ProcessDTO> T buildProcessFromDTO(
            IProcessBuilder<T, D> builder,
            ProcessDTO dto
    ) {
        return builder.buildFromDTO((D) dto);
    }

    @Override
    public BaseProcess createProcess(EnumTypeProcess pTypeProcess, Long pDegreeWorkId) {
        Function<Long, BaseProcess> vCreator = simpleCreators.get(pTypeProcess);
        if (vCreator == null) {
            throw new ProcessException(EnumTypeExceptions.PROCESS_NOT_SUPPORTED);
        }
        return vCreator.apply(pDegreeWorkId);
    }
}

