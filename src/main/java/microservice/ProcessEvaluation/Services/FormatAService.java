package microservice.ProcessEvaluation.Services;

import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Repositories.FormatARepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service responsible for managing the Format A process.
 * Handles evaluator assignment, evaluation workflows, attempts validation,
 * and state transitions according to the business rules.
 */
@Service
public class FormatAService extends ProcessService<FormatA,FormatARepository> {

    private final byte maxAttempts = 3;

    /**
     * Creates a new FormatAService using the provided repository and process factory.
     *
     * @param repository repository used for Format A persistence
     * @param processFactory factory responsible for creating process instances
     * @param pPublisher event publisher for notifications and status changes
     */
    @Autowired
    public FormatAService(FormatARepository repository, ProcessFactory processFactory, Publisher pPublisher) {
        super(repository, processFactory, pPublisher);
    }

    /**
     * Retrieves the approved Format A associated with the given DegreeWork identifier.
     *
     * @param pDegreeWorkId DegreeWork identifier
     * @return approved Format A or null if not found
     */
    @Override
    public FormatA getApproved(Long pDegreeWorkId) {
        return repository.findByDegreeWorkAndGeneralStatus(
                pDegreeWorkId, EnumDegreeWorkStateType.DRAFT
        ).orElse(null);
    }

    /**
     * Finds a Format A pending evaluation for the given evaluator.
     *
     * @param pDegreeWorkId DegreeWork identifier
     * @param pEvaluatorId evaluator identifier
     * @return pending Format A
     */
    @Override
    protected FormatA searchPendingEvaluate(Long pDegreeWorkId, Long pEvaluatorId) {
        return repository.findByDegreeWorkIdAndEvaluationStatus(
                pDegreeWorkId, EnumProcessStatus.PENDING
        ).orElseThrow(() -> new ProcessException(EnumTypeExceptions.NOT_FOUND));
    }

    /**
     * Validates whether Format A meets process rules before further actions.
     * Marks the process as failed when max attempts are exceeded.
     *
     * @param pCurrentProcess current Format A instance
     */
    @Override
    protected void validateRequirements(FormatA pCurrentProcess) {
        if(pCurrentProcess.getAttempts() >= maxAttempts){
            pCurrentProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_FAILED);
            pCurrentProcess.getEvaluation().setFailedStatus();
        }
    }

    /**
     * Executes an evaluator review of Format A and updates process status.
     *
     * @param pProcess process under evaluation
     * @param pIdEvaluator evaluator identifier
     * @param pNewStatus new evaluation status
     * @param pComment evaluation comment
     */
    @Override
    protected void executeEvaluation(FormatA pProcess, Long pIdEvaluator,
                                     EnumProcessStatus pNewStatus, String pComment) {
        pProcess.getEvaluation().addOnlyEvaluator(pIdEvaluator);
        pProcess.getEvaluation().evaluate(pNewStatus,pComment);
        updateInternalData(pProcess);
    }

    /**
     * Updates Format A internal fields after evaluation.
     *
     * @param pProcess process to update
     */
    private void updateInternalData(FormatA pProcess){
        if(pProcess.isApproved())
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.DRAFT);
        else{
            pProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_REJECTED);
            pProcess.increaseAttempts();
        }
    }

    /**
     * Assigns an evaluator to a Format A process.
     *
     * @param pProcess process to assign
     * @param pIdEvaluator evaluator identifier
     */
    @Override
    protected void executeAssignment(FormatA pProcess, Long pIdEvaluator) {
        pProcess.setEvaluator(pIdEvaluator);
    }

    /**
     * Updates Format A status and resets failed evaluation attributes when uploading.
     *
     * @param vCurrentProcess process associated with the upload
     */
    @Override
    protected void executeUpload(FormatA vCurrentProcess) {
        vCurrentProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED);
        vCurrentProcess.getEvaluation().resetFailedEvaluationAttributes();
    }

    /**
     * Validates conditions before creating a new Format A process.
     *
     * @param pNewProcess new Format A instance
     */
    @Override
    protected void validateBeforeCreate(FormatA pNewProcess) {
        pNewProcess.setEvaluation(new Evaluation(null));
    }
}

