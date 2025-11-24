package microservice.ProcessEvaluation.Entities.Builders;

import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Interfaces.IProcessBuilder;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import org.springframework.stereotype.Component;

/**
 * Builder for FormatA process.
 * Converts FormatADTO to FormatA entity.
 */
@Component
public class FormatABuilder implements IProcessBuilder<FormatA, ProcessDTO> {

    @Override
    public FormatA buildFromDTO(ProcessDTO pDto) {
        CoreProcess vNewProcess = new CoreProcess(pDto.getDegreeWorkId());
        FormatA vFormatA = new FormatA(vNewProcess, pDto.getUrl());
        vFormatA.setEvaluation(new Evaluation());
        vFormatA.getEvaluation().setEvaluatorId(1L);//Referencia al coordinador.
        return vFormatA;
    }
}
