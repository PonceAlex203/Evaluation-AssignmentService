package microservice.ProcessEvaluation.Dtos.Mapper;

import microservice.ProcessEvaluation.Dtos.Output.ProcessResponseDTO;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import microservice.ProcessEvaluation.Interfaces.IMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
/**
 * Factory for retrieving process mappers by type
 */
public class ProcessMapperFactory {

    private final Map<EnumTypeProcess, IMapper<?, ?>> mapperMap = new HashMap<>();

    @Autowired
    public ProcessMapperFactory(DraftMapper pDraftMapper, FormatAMapper pFormatAMapper) {
        mapperMap.put(EnumTypeProcess.DRAFT, pDraftMapper);
        mapperMap.put(EnumTypeProcess.FORMAT_A,pFormatAMapper);
    }

    /**
     * Retrieves mapper for the specified process type
     * @param pType the process type
     * @return the appropriate mapper instance
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseProcess, R extends ProcessResponseDTO>
    IMapper<T, R> getMapper(EnumTypeProcess pType) {
        return (IMapper<T, R>) mapperMap.get(pType);
    }
}