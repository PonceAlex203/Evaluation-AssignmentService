package microservice.ProcessEvaluation.Entities.Process;


import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FormatA} class.
 */
@ExtendWith(MockitoExtension.class)
class FormatATest {

    private FormatA formatA;
    private CoreProcess coreProcess;

    @BeforeEach
    void setUp() {
        coreProcess = new CoreProcess(123L);
        formatA = new FormatA(coreProcess, "http://example.com/format-a.pdf");
    }

    @Test
    void defaultConstructor_ShouldInitializeDefaultEvaluatorAndStatus() {
        // Arrange & Act
        FormatA emptyFormatA = new FormatA();

        // Assert
        assertNotNull(emptyFormatA.getEvaluation());
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, emptyFormatA.getGeneralStatus());
        assertEquals(1, emptyFormatA.getAttempts());
    }

    @Test
    void constructor_ShouldInitializeDefaultEvaluatorAndStatus() {
        // Assert
        assertNotNull(formatA.getEvaluation());
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, formatA.getGeneralStatus());
        assertEquals(1, formatA.getAttempts());
    }

    @Test
    void getTypeProcess_ShouldReturnFormatA() {
        // Act & Assert
        assertEquals(EnumTypeProcess.FORMAT_A, formatA.getTypeProcess());
    }

    @Test
    void getAttempts_ShouldReturnCurrentAttempts() {
        // Act & Assert
        assertEquals(1, formatA.getAttempts());
    }

    @Test
    void increaseAttempts_ShouldIncrementAttempts() {
        // Act
        formatA.increaseAttempts();

        // Assert
        assertEquals(2, formatA.getAttempts());
    }

    @Test
    void increaseAttempts_MultipleTimes_ShouldIncrementCorrectly() {
        // Act
        formatA.increaseAttempts();
        formatA.increaseAttempts();
        formatA.increaseAttempts();

        // Assert
        assertEquals(4, formatA.getAttempts());
    }

    @Test
    void assignDefaultEvaluator_ShouldCreateEvaluationWithNullEvaluator() {
        // The constructor already calls assignDefaultEvaluator()
        // Let's verify the result

        // Assert
        assertNotNull(formatA.getEvaluation());
        assertNull(formatA.getEvaluation().getEvaluatorId());
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, formatA.getGeneralStatus());
    }
}