package microservice.ProcessEvaluation.Entities.Builders;

import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Interfaces.IProcessBuilder;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import org.springframework.stereotype.Component;

/**
 * Builder implementation for creating {@link FormatA} processes from a {@link ProcessDTO}.
 */
@Component
public class FormatABuilder implements IProcessBuilder<FormatA, ProcessDTO> {

    /**
     * Builds a FormatA process using the provided DTO.
     *
     * @param pDto the data transfer object containing process information
     * @return a new FormatA instance
     */
    @Override
    public FormatA buildFromDTO(ProcessDTO pDto) {
        CoreProcess vNewProcess = new CoreProcess(pDto.getDegreeWorkId());
        return new FormatA(vNewProcess, pDto.getUrl());
    }
}

