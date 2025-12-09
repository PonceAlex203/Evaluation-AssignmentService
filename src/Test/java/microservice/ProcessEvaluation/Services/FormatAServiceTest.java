package microservice.ProcessEvaluation.Services;

import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Repositories.FormatARepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

/**
 * Unit tests for {@link FormatAService}.
 */
@ExtendWith(MockitoExtension.class)
class FormatAServiceTest {

    @Mock
    private FormatARepository repository;

    @Mock
    private ProcessFactory processFactory;

    @Mock
    private Publisher publisher;

    private FormatAService formatAService;
    private FormatA realFormatA;
    private Evaluation realEvaluation;

    @BeforeEach
    void setUp() {
        formatAService = new FormatAService(repository, processFactory, publisher);

        // Crear objetos reales en lugar de mocks para evitar NullPointerException
        CoreProcess coreProcess = new CoreProcess(1L);
        realFormatA = new FormatA(coreProcess, "http://example.com/test.pdf");
        realEvaluation = new Evaluation(null);
        realFormatA.setEvaluation(realEvaluation);
    }

    @Test
    void getApproved_WhenDraftExists_ShouldReturnFormatA() {
        // Arrange
        Long degreeWorkId = 1L;
        FormatA approvedFormatA = new FormatA(new CoreProcess(degreeWorkId), "test.pdf");
        when(repository.findByDegreeWorkAndGeneralStatus(degreeWorkId, EnumDegreeWorkStateType.DRAFT))
                .thenReturn(Optional.of(approvedFormatA));

        // Act
        FormatA result = formatAService.getApproved(degreeWorkId);

        // Assert
        assertEquals(approvedFormatA, result);
        verify(repository).findByDegreeWorkAndGeneralStatus(degreeWorkId, EnumDegreeWorkStateType.DRAFT);
    }

    @Test
    void getApproved_WhenNoDraftExists_ShouldReturnNull() {
        // Arrange
        Long degreeWorkId = 999L;
        when(repository.findByDegreeWorkAndGeneralStatus(degreeWorkId, EnumDegreeWorkStateType.DRAFT))
                .thenReturn(Optional.empty());

        // Act
        FormatA result = formatAService.getApproved(degreeWorkId);

        // Assert
        assertNull(result);
        verify(repository).findByDegreeWorkAndGeneralStatus(degreeWorkId, EnumDegreeWorkStateType.DRAFT);
    }

    @Test
    void searchPendingEvaluate_WhenPendingFormatAExists_ShouldReturnFormatA() {
        // Arrange
        Long degreeWorkId = 1L;
        Long evaluatorId = 100L;
        when(repository.findByDegreeWorkIdAndEvaluationStatus(degreeWorkId, EnumProcessStatus.PENDING))
                .thenReturn(Optional.of(realFormatA));

        // Act
        FormatA result = formatAService.searchPendingEvaluate(degreeWorkId, evaluatorId);

        // Assert
        assertEquals(realFormatA, result);
        verify(repository).findByDegreeWorkIdAndEvaluationStatus(degreeWorkId, EnumProcessStatus.PENDING);
    }

    @Test
    void searchPendingEvaluate_WhenNoPendingFormatA_ShouldThrowException() {
        // Arrange
        Long degreeWorkId = 999L;
        Long evaluatorId = 100L;
        when(repository.findByDegreeWorkIdAndEvaluationStatus(degreeWorkId, EnumProcessStatus.PENDING))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            formatAService.searchPendingEvaluate(degreeWorkId, evaluatorId);
        });

        assertEquals(EnumTypeExceptions.NOT_FOUND, exception.getType());
        verify(repository).findByDegreeWorkIdAndEvaluationStatus(degreeWorkId, EnumProcessStatus.PENDING);
    }

    @Test
    void validateRequirements_WhenAttemptsLessThanMax_ShouldNotChangeStatus() {
        // Arrange - Usar objeto real
        realFormatA = new FormatA(new CoreProcess(1L), "test.pdf");
        Evaluation eval = new Evaluation(null);
        realFormatA.setEvaluation(eval);
        // attempts = 1 por defecto

        // Act
        formatAService.validateRequirements(realFormatA);

        // Assert - Verificar que no se llamó a setGeneralStatus
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, realFormatA.getGeneralStatus());
    }

    @Test
    void validateRequirements_WhenAttemptsEqualToMax_ShouldMarkAsFailed() {
        // Arrange - Crear FormatA con attempts = 3
        realFormatA = new FormatA(new CoreProcess(1L), "test.pdf");
        Evaluation eval = new Evaluation(null);
        realFormatA.setEvaluation(eval);

        // Configurar attempts = 3 usando reflection
        try {
            var field = FormatA.class.getDeclaredField("attempts");
            field.setAccessible(true);
            field.set(realFormatA, (byte) 3);
        } catch (Exception e) {
            fail("Failed to set attempts field: " + e.getMessage());
        }

        // Act
        formatAService.validateRequirements(realFormatA);

        // Assert
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_FAILED, realFormatA.getGeneralStatus());
        assertEquals(EnumProcessStatus.FAILED, realFormatA.getEvaluation().getEvaluationStatus());
    }

    @Test
    void executeEvaluation_ShouldUpdateEvaluationAndInternalData() {
        // Arrange
        Long evaluatorId = 100L;
        EnumProcessStatus newStatus = EnumProcessStatus.APPROVED;
        String comment = "Good work";

        FormatA formatA = new FormatA(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation = new Evaluation(null);
        formatA.setEvaluation(evaluation);

        // Act
        formatAService.executeEvaluation(formatA, evaluatorId, newStatus, comment);

        // Assert
        assertEquals(evaluatorId, evaluation.getEvaluatorId());
        assertEquals(newStatus, evaluation.getEvaluationStatus());
        assertEquals(comment, evaluation.getComment());
        assertNotNull(evaluation.getEvaluationDate());
        assertEquals(EnumDegreeWorkStateType.DRAFT, formatA.getGeneralStatus());
    }

    @Test
    void executeEvaluation_WhenNotApproved_ShouldMarkAsRejectedAndIncreaseAttempts() {
        // Arrange
        Long evaluatorId = 100L;
        EnumProcessStatus newStatus = EnumProcessStatus.REJECTED;
        String comment = "Needs improvement";

        FormatA formatA = new FormatA(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation = new Evaluation(null);
        formatA.setEvaluation(evaluation);

        // Act
        formatAService.executeEvaluation(formatA, evaluatorId, newStatus, comment);

        // Assert
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_REJECTED, formatA.getGeneralStatus());
        assertEquals(2, formatA.getAttempts()); // Se incrementó de 1 a 2
    }

    @Test
    void executeAssignment_ShouldSetEvaluator() {
        // Arrange
        Long evaluatorId = 100L;
        FormatA formatA = new FormatA(new CoreProcess(1L), "test.pdf");

        // Act
        formatAService.executeAssignment(formatA, evaluatorId);

        // Assert
        assertEquals(evaluatorId, formatA.getEvaluation().getEvaluatorId());
    }

    @Test
    void executeUpload_ShouldUpdateStatusAndResetEvaluation() {
        // Arrange
        FormatA formatA = new FormatA(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation = new Evaluation(100L);
        evaluation.evaluate(EnumProcessStatus.FAILED, "Failed");
        formatA.setEvaluation(evaluation);

        // Act
        formatAService.executeUpload(formatA);

        // Assert
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, formatA.getGeneralStatus());
        assertEquals(EnumProcessStatus.PENDING, evaluation.getEvaluationStatus());
        assertNull(evaluation.getComment());
        assertNull(evaluation.getEvaluationDate());
    }

    @Test
    void validateBeforeCreate_ShouldSetNewEvaluation() {
        // Arrange
        FormatA newFormatA = new FormatA(new CoreProcess(1L), "test.pdf");

        // Act
        formatAService.validateBeforeCreate(newFormatA);

        // Assert
        assertNotNull(newFormatA.getEvaluation());
    }

}