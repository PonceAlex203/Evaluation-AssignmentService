package microservice.ProcessEvaluation.Dtos.Mapper;

import microservice.ProcessEvaluation.Dtos.Output.ProcessResponseDTO;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Interfaces.IMapper;

/**
 * Abstract base mapper for process entities to DTOs
 * @param <T> the process entity type
 * @param <R> the response DTO type
 */
public abstract class BaseProcessMapper<T extends BaseProcess, R extends ProcessResponseDTO>
        implements IMapper<T, R> {

    @Override
    public R toDto(T pEntity) {
        R vDto = createResponseInstance();
        vDto.setDegreeWorkId(pEntity.getDegreeworkId());
        vDto.setDate(pEntity.getCore().getDate());
        vDto.setUrl(pEntity.getUrl());

        mapSpecificFields(pEntity, vDto);
        return vDto;
    }

    /**
     * Creates a new instance of the response DTO
     * @return the DTO instance
     */
    protected abstract R createResponseInstance();

    /**
     * Maps entity-specific fields to the DTO
     * @param pEntity the source entity
     * @param pDto the target DTO
     */
    protected abstract void mapSpecificFields(T pEntity, R pDto);
}
