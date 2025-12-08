package microservice.ProcessEvaluation.Entities.Builders;

import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Interfaces.IProcessBuilder;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import org.springframework.stereotype.Component;

/**
 * Builder implementation for creating {@link Draft} processes from a {@link ProcessDTO}.
 */
@Component
public class DraftBuilder implements IProcessBuilder<Draft, ProcessDTO> {

    /**
     * Builds a Draft process using the provided DTO.
     *
     * @param pDTO the data transfer object containing process information
     * @return a new Draft instance
     */
    @Override
    public Draft buildFromDTO(ProcessDTO pDTO) {
        CoreProcess vNewProcess = new CoreProcess(pDTO.getDegreeWorkId());
        return new Draft(vNewProcess, pDTO.getUrl());
    }
}


