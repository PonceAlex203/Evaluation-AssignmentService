package microservice.ProcessEvaluation.Dtos.Mapper;

import microservice.ProcessEvaluation.Dtos.Output.FormatAResponseDTO;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import org.springframework.stereotype.Component;

@Component
public class FormatAMapper extends BaseProcessMapper<FormatA, FormatAResponseDTO> {
    @Override
    protected FormatAResponseDTO createResponseInstance() {
        return new FormatAResponseDTO();
    }

    @Override
    protected void mapSpecificFields(FormatA pEntity, FormatAResponseDTO pDto) {
        pDto.setAttempts(pEntity.getAttempts());
    }
}
