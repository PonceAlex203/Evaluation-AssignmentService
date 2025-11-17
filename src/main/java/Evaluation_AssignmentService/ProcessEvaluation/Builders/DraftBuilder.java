package Evaluation_AssignmentService.ProcessEvaluation.Builders;

import Evaluation_AssignmentService.ProcessEvaluation.Dto.DraftDTO;
import Evaluation_AssignmentService.ProcessEvaluation.Interface.IProcessBuilder;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.Draft;
import org.springframework.stereotype.Component;

/**
 * Builder for Draft process.
 * Converts DraftDTO to Draft entity.
 */
@Component
public class DraftBuilder implements IProcessBuilder<Draft, DraftDTO> {

    /**
     * Builds a Draft entity from a DraftDTO.
     * @param pDTO DTO containing draft data
     * @return Draft entity
     */
    @Override
    public Draft buildFromDTO(DraftDTO pDTO) {
        return new Draft(pDTO.getDegreeWorkId(), pDTO.getUrl());
    }
}

