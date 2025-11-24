package microservice.ProcessEvaluation.Interfaces;

import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Entities.Process.BaseProcess;

/**
 * Generic interface for building process entities from DTOs.
 * @param <T> type of process entity
 * @param <D> type of process DTO
 */
public interface IProcessBuilder<T extends BaseProcess, D extends ProcessDTO> {

    /**
     * Builds a process entity from the given DTO.
     * @param dto DTO containing process data
     * @return process entity
     */
    T buildFromDTO(D dto);
}
