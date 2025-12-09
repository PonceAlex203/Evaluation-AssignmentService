package microservice.ProcessEvaluation.Entities.Process;

import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for abstract {@link BaseProcess} class.
 * Uses a concrete implementation for testing.
 */
@ExtendWith(MockitoExtension.class)
class BaseProcessTest {

    // Concrete implementation for testing abstract class
    private static class TestProcess extends BaseProcess {
        public TestProcess() {
            super();
        }

        public TestProcess(CoreProcess pCore, String pUrl) {
            super(pCore, pUrl);
        }

        @Override
        public EnumTypeProcess getTypeProcess() {
            return null;
        }
    }

    private TestProcess testProcess;
    private CoreProcess coreProcess;

    @BeforeEach
    void setUp() {
        coreProcess = new CoreProcess(123L);
        testProcess = new TestProcess(coreProcess, "http://example.com/test.pdf");
    }

    @Test
    void constructor_ShouldInitializeCoreAndUrl() {
        // Arrange & Act already done in setUp()

        // Assert
        assertNotNull(testProcess.getCore());
        assertEquals(123L, testProcess.getCore().getDegreeWorkId());
        assertEquals("http://example.com/test.pdf", testProcess.getUrl());
    }

    @Test
    void constructor_WithNullUrl_ShouldInitializeWithNullUrl() {
        // Arrange & Act
        TestProcess process = new TestProcess(coreProcess, null);

        // Assert
        assertNull(process.getUrl());
    }

    @Test
    void getDegreeworkId_ShouldReturnCoreDegreeWorkId() {
        // Act
        Long degreeworkId = testProcess.getDegreeworkId();

        // Assert
        assertEquals(123L, degreeworkId);
    }

    @Test
    void setUrl_ShouldUpdateUrl() {
        // Arrange
        String newUrl = "http://example.com/new.pdf";

        // Act
        testProcess.setUrl(newUrl);

        // Assert
        assertEquals(newUrl, testProcess.getUrl());
    }

    @Test
    void setEvaluation_ShouldUpdateEvaluation() {
        // Arrange
        Evaluation evaluation = new Evaluation(456L);

        // Act
        testProcess.setEvaluation(evaluation);

        // Assert
        assertNotNull(testProcess.getEvaluation());
        assertEquals(456L, testProcess.getEvaluation().getEvaluatorId());
    }

    @Test
    void setGeneralStatus_ShouldUpdateStatus() {
        // Act
        testProcess.setGeneralStatus(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED);

        // Assert
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, testProcess.getGeneralStatus());
    }

    @Test
    void setEvaluator_ShouldSetEvaluatorInEvaluation() {
        // Arrange
        Evaluation evaluation = new Evaluation();
        testProcess.setEvaluation(evaluation);
        Long evaluatorId = 789L;

        // Act
        testProcess.setEvaluator(evaluatorId);

        // Assert
        assertEquals(evaluatorId, testProcess.getEvaluation().getEvaluatorId());
    }

    @Test
    void isApproved_WithApprovedEvaluation_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation = new Evaluation(456L);
        evaluation.evaluate(EnumProcessStatus.APPROVED, "Good work");
        testProcess.setEvaluation(evaluation);

        // Act & Assert
        assertTrue(testProcess.isApproved());
    }

    @Test
    void isApproved_WithNullEvaluation_ShouldReturnFalse() {
        // Arrange
        testProcess.setEvaluation(null);

        // Act & Assert
        assertFalse(testProcess.isApproved());
    }

    @Test
    void isEvaluated_WithEvaluatedEvaluation_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation = new Evaluation(456L);
        evaluation.evaluate(EnumProcessStatus.REJECTED, "Needs improvement");
        testProcess.setEvaluation(evaluation);

        // Act & Assert
        assertTrue(testProcess.isEvaluated());
    }

    @Test
    void isEvaluated_WithPendingEvaluation_ShouldReturnFalse() {
        // Arrange
        Evaluation evaluation = new Evaluation(456L);
        testProcess.setEvaluation(evaluation);

        // Act & Assert
        assertFalse(testProcess.isEvaluated());
    }

    @Test
    void isAssigned_WithEvaluation_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation = new Evaluation(456L);
        testProcess.setEvaluation(evaluation);

        // Act & Assert
        assertTrue(testProcess.isAssigned());
    }

    @Test
    void isAssigned_WithNullEvaluation_ShouldReturnFalse() {
        // Arrange
        testProcess.setEvaluation(null);

        // Act & Assert
        assertFalse(testProcess.isAssigned());
    }

    @Test
    void isEvaluator_WithMatchingEvaluatorId_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation = new Evaluation(100L);
        testProcess.setEvaluation(evaluation);

        // Act & Assert
        assertTrue(testProcess.isEvaluator(100L));
    }

    @Test
    void isEvaluator_WithNonMatchingEvaluatorId_ShouldReturnFalse() {
        // Arrange
        Evaluation evaluation = new Evaluation(456L);
        testProcess.setEvaluation(evaluation);

        // Act & Assert
        assertFalse(testProcess.isEvaluator(999L));
    }

    @Test
    void isEvaluator_WithNullEvaluation_ShouldReturnFalse() {
        // Arrange
        testProcess.setEvaluation(null);

        // Act & Assert
        assertFalse(testProcess.isEvaluator(456L));
    }
}