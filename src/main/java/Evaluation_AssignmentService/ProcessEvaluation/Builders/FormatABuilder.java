package Evaluation_AssignmentService.ProcessEvaluation.Builders;

import Evaluation_AssignmentService.ProcessEvaluation.Dto.FormatADTO;
import Evaluation_AssignmentService.ProcessEvaluation.Interface.IProcessBuilder;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.FormatA;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.springframework.stereotype.Component;

/**
 * Builder for FormatA process.
 * Converts FormatADTO to FormatA entity.
 */
@Component
public class FormatABuilder implements IProcessBuilder<FormatA, FormatADTO> {

    /**
     * Builds a FormatA entity from a FormatADTO.
     * Throws exception if degreeWorkId is null.
     * @param dto DTO containing format A data
     * @return FormatA entity
     * @throws ProcessException if degreeWorkId is null
     */
    @Override
    public FormatA buildFromDTO(FormatADTO dto) {
        if (dto.getDegreeWorkId() == null) {
            throw new ProcessException(EnumTypeExceptions.NULL_PARAMETER);
        }
        return new FormatA(dto.getDegreeWorkId(), dto.getUrl());
    }
}
