package microservice.ProcessEvaluation.Entities.Builders;

import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Interfaces.IProcessBuilder;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import org.springframework.stereotype.Component;

/**
 * Builder for Draft process.
 * Converts DraftDTO to Draft entity.
 */
@Component
public class DraftBuilder implements IProcessBuilder<Draft, ProcessDTO> {


    @Override
    public Draft buildFromDTO(ProcessDTO pDTO) {
        CoreProcess vNewProcess = new CoreProcess(pDTO.getDegreeWorkId());
        return new Draft(vNewProcess, pDTO.getUrl());
    }
}

