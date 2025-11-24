package microservice.ProcessEvaluation.Dtos.Mapper;

import microservice.ProcessEvaluation.Dtos.Output.ProcessResponseDTO;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Interfaces.IMapper;

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
    protected abstract R createResponseInstance();
    protected abstract void mapSpecificFields(T pEntity, R pDto);
}
