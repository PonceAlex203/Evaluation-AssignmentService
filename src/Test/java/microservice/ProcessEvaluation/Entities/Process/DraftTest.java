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
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Unit tests for {@link Draft} class.
 */
@ExtendWith(MockitoExtension.class)
class DraftTest {

    private Draft draft;
    private CoreProcess coreProcess;

    @BeforeEach
    void setUp() {
        coreProcess = new CoreProcess(123L);
        draft = new Draft(coreProcess, "http://example.com/draft.pdf");
    }

    @Test
    void defaultConstructor_ShouldInitializeStatus() {
        // Arrange & Act
        Draft emptyDraft = new Draft();

        // Assert
        assertEquals(EnumDegreeWorkStateType.DRAFT_SUBMITTED, emptyDraft.getGeneralStatus());
    }

    @Test
    void constructor_ShouldInitializeStatus() {
        // Assert
        assertEquals(EnumDegreeWorkStateType.DRAFT_SUBMITTED, draft.getGeneralStatus());
    }

    @Test
    void getTypeProcess_ShouldReturnDraft() {
        // Act & Assert
        assertEquals(EnumTypeProcess.DRAFT, draft.getTypeProcess());
    }

    @Test
    void setEvaluation2_ShouldSetSecondaryEvaluation() {
        // Arrange
        Evaluation evaluation2 = new Evaluation(456L);

        // Act
        draft.setEvaluation2(evaluation2);

        // Assert
        assertNotNull(draft.getEvaluation2());
        assertEquals(456L, draft.getEvaluation2().getEvaluatorId());
    }

    @Test
    void setEvaluator2_ShouldSetEvaluatorInSecondaryEvaluation() {
        // Arrange
        Evaluation evaluation2 = new Evaluation();
        draft.setEvaluation2(evaluation2);
        Long evaluatorId = 789L;

        // Act
        draft.setEvaluator2(evaluatorId);

        // Assert
        assertEquals(evaluatorId, draft.getEvaluation2().getEvaluatorId());
    }

    @Test
    void isBothEvaluated_WithBothEvaluated_ShouldReturnTrue() {
        // Arrange
        Evaluation eval1 = new Evaluation(100L);
        eval1.evaluate(EnumProcessStatus.APPROVED, "Good");
        draft.setEvaluation(eval1);

        Evaluation eval2 = new Evaluation(200L);
        eval2.evaluate(EnumProcessStatus.APPROVED, "Also good");
        draft.setEvaluation2(eval2);

        // Act & Assert
        assertTrue(draft.isBothEvaluated());
    }

    @Test
    void isBothEvaluated_WithOnlyFirstEvaluated_ShouldReturnFalse() {
        // Arrange
        Evaluation eval1 = new Evaluation(100L);
        eval1.evaluate(EnumProcessStatus.APPROVED, "Good");
        draft.setEvaluation(eval1);

        Evaluation eval2 = new Evaluation(200L);
        draft.setEvaluation2(eval2);

        // Act & Assert
        assertFalse(draft.isBothEvaluated());
    }

    @Test
    void isAssigned2_WithSecondaryEvaluation_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation2 = new Evaluation(456L);
        draft.setEvaluation2(evaluation2);

        // Act & Assert
        assertTrue(draft.isAssigned2());
    }

    @Test
    void isAssigned2_WithNullSecondaryEvaluation_ShouldReturnFalse() {
        // Act & Assert
        assertFalse(draft.isAssigned2());
    }

    @Test
    void isEvaluated2_WithEvaluatedSecondaryEvaluation_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation2 = new Evaluation(456L);
        evaluation2.evaluate(EnumProcessStatus.APPROVED, "Approved");
        draft.setEvaluation2(evaluation2);

        // Act & Assert
        assertTrue(draft.isEvaluated2());
    }

    @Test
    void isEvaluated2_WithPendingSecondaryEvaluation_ShouldReturnFalse() {
        // Arrange
        Evaluation evaluation2 = new Evaluation(456L);
        draft.setEvaluation2(evaluation2);

        // Act & Assert
        assertFalse(draft.isEvaluated2());
    }

    @Test
    void isApproved2_WithApprovedSecondaryEvaluation_ShouldReturnTrue() {
        // Arrange
        Evaluation evaluation2 = new Evaluation(456L);
        evaluation2.evaluate(EnumProcessStatus.APPROVED, "Secondary approved");
        draft.setEvaluation2(evaluation2);

        // Act & Assert
        assertTrue(draft.isApproved2());
    }

    @Test
    void isApproved2_WithRejectedSecondaryEvaluation_ShouldReturnFalse() {
        // Arrange
        Evaluation evaluation2 = new Evaluation(456L);
        evaluation2.evaluate(EnumProcessStatus.REJECTED, "Secondary rejected");
        draft.setEvaluation2(evaluation2);

        // Act & Assert
        assertFalse(draft.isApproved2());
    }

    @Test
    void isExpired_WithFutureDeadline_ShouldReturnFalse() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date futureDate = calendar.getTime();

        // Use reflection to set private field for testing
        try {
            var field = Draft.class.getDeclaredField("deadline");
            field.setAccessible(true);
            field.set(draft, futureDate);
        } catch (Exception e) {
            fail("Failed to set deadline field: " + e.getMessage());
        }

        // Act & Assert
        assertFalse(draft.isExpired());
    }

    @Test
    void isExpired_WithPastDeadline_ShouldReturnTrue() throws Exception {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date pastDate = calendar.getTime();

        // Use reflection to set private field for testing
        var field = Draft.class.getDeclaredField("deadline");
        field.setAccessible(true);
        field.set(draft, pastDate);

        // Act & Assert
        assertTrue(draft.isExpired());
    }

    @Test
    void isExpired_WithNullDeadline_ShouldReturnNull() {
        // Act & Assert
        assertNull(draft.getDeadline());
    }

    @Test
    void initializeDeadline_ShouldCalculate90DaysFromCoreDate() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1);
        Date coreDate = calendar.getTime();

        CoreProcess mockCore = mock(CoreProcess.class);
        when(mockCore.getDate()).thenReturn(coreDate);

        Draft testDraft = new Draft(mockCore, "http://example.com/test.pdf");

        // Act - Simulate @PrePersist
        try {
            var method = Draft.class.getDeclaredMethod("initializeDeadline");
            method.setAccessible(true);
            method.invoke(testDraft);
        } catch (Exception e) {
            fail("Failed to invoke initializeDeadline: " + e.getMessage());
        }

        // Assert
        assertNotNull(testDraft.getDeadline());

        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(coreDate);
        expectedCalendar.add(Calendar.DAY_OF_YEAR, 90);
        Date expectedDeadline = expectedCalendar.getTime();

        // Compare dates without milliseconds
        long expectedTime = expectedDeadline.getTime() / 1000;
        long actualTime = testDraft.getDeadline().getTime() / 1000;
        assertEquals(expectedTime, actualTime);
    }
}