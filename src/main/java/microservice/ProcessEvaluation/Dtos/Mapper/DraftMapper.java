package microservice.ProcessEvaluation.Dtos.Mapper;

import microservice.ProcessEvaluation.Dtos.Output.DraftResponseDTO;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import org.springframework.stereotype.Component;

@Component
public class DraftMapper extends BaseProcessMapper<Draft, DraftResponseDTO> {
    @Override
    protected DraftResponseDTO createResponseInstance() {
        return new DraftResponseDTO();
    }

    @Override
    protected void mapSpecificFields(Draft pEntity, DraftResponseDTO pDto) {
        pDto.setDeadline(pEntity.getDeadline());
    }
}
