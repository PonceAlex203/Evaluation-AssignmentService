package Evaluation_AssignmentService.Interface;

import Evaluation_AssignmentService.Dto.ProcessDTO;
import Evaluation_AssignmentService.Enum.EnumTypeProcess;
import Evaluation_AssignmentService.ProcessEntity.BaseProcess;

/**
 * Factory interface for creating process entities.
 */
public interface IProcessFactory {

    /**
     * Creates a process entity based on the given type and degree work ID.
     * @param pTypeProcess type of process to create
     * @param pDegreeWorkId ID of the related degree work
     * @return created process entity
     */
    BaseProcess createProcess(EnumTypeProcess pTypeProcess, Long pDegreeWorkId);

    /**
     * Creates a process entity using the provided DTO.
     * @param dto DTO containing process data
     * @return created process entity
     */
    BaseProcess createProcessFromDTO(ProcessDTO dto);
}
