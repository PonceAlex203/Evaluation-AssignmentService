package microservice.ProcessEvaluation.Services;


import microservice.Comunication.Publisher.Publisher;
import microservice.ProcessEvaluation.Entities.Base.CoreProcess;
import microservice.ProcessEvaluation.Entities.Factories.ProcessFactory;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Entities.Process.Evaluation;
import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumProcessStatus;
import microservice.ProcessEvaluation.Repositories.DraftRepository;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Unit tests for {@link DraftService}.
 */
@ExtendWith(MockitoExtension.class)
class DraftServiceTest {

    @Mock
    private DraftRepository repository;

    @Mock
    private ProcessFactory processFactory;

    @Mock
    private FormatAService formatAService;

    @Mock
    private Publisher publisher;

    private DraftService draftService;

    @BeforeEach
    void setUp() {
        draftService = new DraftService(repository, processFactory, formatAService, publisher);
    }

    @Test
    void searchUnassignedBy_WhenUnassignedDraftExists_ShouldReturnDraft() {
        // Arrange
        Long degreeWorkId = 1L;
        Draft draft = new Draft(new CoreProcess(degreeWorkId), "test.pdf");
        when(repository.findUnassignedDraft(degreeWorkId)).thenReturn(Optional.of(draft));

        // Act
        Draft result = draftService.searchUnassignedBy(degreeWorkId);

        // Assert
        assertEquals(draft, result);
        verify(repository).findUnassignedDraft(degreeWorkId);
    }

    @Test
    void searchUnassignedBy_WhenNoUnassignedDraft_ShouldThrowException() {
        // Arrange
        Long degreeWorkId = 999L;
        when(repository.findUnassignedDraft(degreeWorkId)).thenReturn(Optional.empty());

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            draftService.searchUnassignedBy(degreeWorkId);
        });

        assertEquals(EnumTypeExceptions.NOT_FOUND, exception.getType());
        verify(repository).findUnassignedDraft(degreeWorkId);
    }

    @Test
    void searchPendingEvaluate_WhenPendingDraftExists_ShouldReturnDraft() {
        // Arrange
        Long degreeWorkId = 1L;
        Long evaluatorId = 100L;
        Draft draft = new Draft(new CoreProcess(degreeWorkId), "test.pdf");
        when(repository.findPendingEvaluate(degreeWorkId, evaluatorId, EnumProcessStatus.PENDING))
                .thenReturn(Optional.of(draft));

        // Act
        Draft result = draftService.searchPendingEvaluate(degreeWorkId, evaluatorId);

        // Assert
        assertEquals(draft, result);
        verify(repository).findPendingEvaluate(degreeWorkId, evaluatorId, EnumProcessStatus.PENDING);
    }

    @Test
    void searchPendingAssignment_WhenPendingAssignmentDraftExists_ShouldReturnDraft() {
        // Arrange
        Long degreeWorkId = 1L;
        Draft draft = new Draft(new CoreProcess(degreeWorkId), "test.pdf");
        List<EnumDegreeWorkStateType> expectedStatuses = Arrays.asList(
                EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED,
                EnumDegreeWorkStateType.DRAFT_SUBMITTED
        );
        when(repository.findAllBy(degreeWorkId, expectedStatuses))
                .thenReturn(Optional.of(draft));

        // Act
        Draft result = draftService.searchPendingAssignment(degreeWorkId);

        // Assert
        assertEquals(draft, result);
        verify(repository).findAllBy(degreeWorkId, expectedStatuses);
    }

    @Test
    void getPendingEvaluate_ShouldReturnPendingDrafts() {
        // Arrange
        Long evaluatorId = 100L;
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        List<Draft> expectedDrafts = Arrays.asList(draft);
        when(repository.findAllBy(evaluatorId, EnumProcessStatus.PENDING))
                .thenReturn(expectedDrafts);

        // Act
        List<Draft> result = draftService.getPendingEvaluate(evaluatorId);

        // Assert
        assertEquals(expectedDrafts, result);
        verify(repository).findAllBy(evaluatorId, EnumProcessStatus.PENDING);
    }

    @Test
    void getPendingAssignment_ShouldReturnDraftsPendingAssignment() {
        // Arrange
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        List<Draft> expectedDrafts = Arrays.asList(draft);
        when(repository.findPendingAssignment()).thenReturn(expectedDrafts);

        // Act
        List<Draft> result = draftService.getPendingAssignment();

        // Assert
        assertEquals(expectedDrafts, result);
        verify(repository).findPendingAssignment();
    }

    @Test
    void executeUpload_ShouldUpdateStatus() {
        // Arrange
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");

        // Act
        draftService.executeUpload(draft);

        // Assert
        assertEquals(EnumDegreeWorkStateType.DRAFT_SUBMITTED, draft.getGeneralStatus());
    }

    @Test
    void getApproved_WhenApprovedDraftExists_ShouldReturnDraft() {
        // Arrange
        Long degreeWorkId = 1L;
        Draft approvedDraft = new Draft(new CoreProcess(degreeWorkId), "approved.pdf");
        when(repository.findByDegreeWorkAndGeneralStatus(degreeWorkId, EnumDegreeWorkStateType.DRAFT_APPROVED))
                .thenReturn(Optional.of(approvedDraft));

        // Act
        Draft result = draftService.getApproved(degreeWorkId);

        // Assert
        assertEquals(approvedDraft, result);
        verify(repository).findByDegreeWorkAndGeneralStatus(degreeWorkId, EnumDegreeWorkStateType.DRAFT_APPROVED);
    }

    @Test
    void validateRequirements_WhenExpired_ShouldThrowException() throws Exception {
        // Arrange
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date pastDate = calendar.getTime();

        var deadlineField = Draft.class.getDeclaredField("deadline");
        deadlineField.setAccessible(true);
        deadlineField.set(draft, pastDate);

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            draftService.validateRequirements(draft);
        });

        assertEquals(EnumTypeExceptions.EXPIRED_TIME, exception.getType());
    }

    @Test
    void validateBeforeCreate_WhenFormatAApproved_ShouldNotThrowException() {
        // Arrange
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        FormatA formatA = new FormatA(new CoreProcess(1L), "formatA.pdf");

        when(formatAService.getApproved(draft.getDegreeworkId())).thenReturn(formatA);

        // Act & Assert
        assertDoesNotThrow(() -> draftService.validateBeforeCreate(draft));
    }

    @Test
    void validateBeforeCreate_WhenFormatANotApproved_ShouldThrowException() {
        // Arrange
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");

        when(formatAService.getApproved(draft.getDegreeworkId())).thenReturn(null);

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            draftService.validateBeforeCreate(draft);
        });

        assertEquals(EnumTypeExceptions.PREVIOUS_PROCESS_NOT_APPROVED, exception.getType());
    }

    @Test
    void executeEvaluation_WhenFirstEvaluator_ShouldEvaluateFirstEvaluation() {
        // Arrange
        Long evaluatorId = 100L;
        EnumProcessStatus newStatus = EnumProcessStatus.APPROVED;
        String comment = "Good";

        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation = new Evaluation(evaluatorId);
        draft.setEvaluation(evaluation);

        // Act
        draftService.executeEvaluation(draft, evaluatorId, newStatus, comment);

        // Assert
        assertEquals(newStatus, evaluation.getEvaluationStatus());
        assertEquals(comment, evaluation.getComment());
        assertEquals(EnumDegreeWorkStateType.FIRST_DRAFT_JURY_EVALUATION, draft.getGeneralStatus());
    }

    @Test
    void executeEvaluation_WhenSecondEvaluator_ShouldEvaluateSecondEvaluation() {
        // Arrange
        Long evaluatorId1 = 100L;
        Long evaluatorId2 = 200L;
        EnumProcessStatus newStatus = EnumProcessStatus.APPROVED;
        String comment = "Good";

        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation1 = new Evaluation(evaluatorId1);
        Evaluation evaluation2 = new Evaluation(evaluatorId2);
        draft.setEvaluation(evaluation1);
        draft.setEvaluation2(evaluation2);

        // Act
        draftService.executeEvaluation(draft, evaluatorId2, newStatus, comment);

        // Assert
        assertEquals(newStatus, evaluation2.getEvaluationStatus());
        assertEquals(comment, evaluation2.getComment());
        assertEquals(EnumDegreeWorkStateType.FIRST_DRAFT_JURY_EVALUATION, draft.getGeneralStatus());
    }

    @Test
    void executeAssignment_WhenFirstAssignment_ShouldSetFirstEvaluator() {
        // Arrange
        Long evaluatorId = 100L;
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");

        // Act
        draftService.executeAssignment(draft, evaluatorId);

        // Assert
        assertNotNull(draft.getEvaluation());
        assertEquals(evaluatorId, draft.getEvaluation().getEvaluatorId());
        assertEquals(EnumDegreeWorkStateType.FIRS_DRAFT_JURY_ASSIGNED, draft.getGeneralStatus());
    }

    @Test
    void executeAssignment_WhenSecondAssignment_ShouldSetSecondEvaluator() {
        // Arrange
        Long evaluatorId1 = 100L;
        Long evaluatorId2 = 200L;
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation1 = new Evaluation(evaluatorId1);
        draft.setEvaluation(evaluation1);

        // Act
        draftService.executeAssignment(draft, evaluatorId2);

        // Assert
        assertNotNull(draft.getEvaluation2());
        assertEquals(evaluatorId2, draft.getEvaluation2().getEvaluatorId());
        assertEquals(EnumDegreeWorkStateType.DRAFT_JURY_ASSIGNED, draft.getGeneralStatus());
    }

    @Test
    void executeAssignment_WhenSameEvaluatorForSecond_ShouldThrowException() {
        // Arrange
        Long evaluatorId = 100L;
        Draft draft = new Draft(new CoreProcess(1L), "test.pdf");
        Evaluation evaluation = new Evaluation(evaluatorId);
        draft.setEvaluation(evaluation);

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            draftService.executeAssignment(draft, evaluatorId);
        });

        assertEquals(EnumTypeExceptions.PREVIOUSLY_ASSIGNED, exception.getType());
    }

    @Test
    void assignedEvaluators_ShouldAssignBothEvaluators() {
        // Arrange
        Long degreeWorkId = 1L;
        Long evaluatorId1 = 100L;
        Long evaluatorId2 = 200L;

        Draft draft = new Draft(new CoreProcess(degreeWorkId), "test.pdf");
        when(repository.findUnassignedDraft(degreeWorkId)).thenReturn(Optional.of(draft));
        when(repository.save(any(Draft.class))).thenReturn(draft);

        // Act
        Draft result = draftService.assignedEvaluators(degreeWorkId, evaluatorId1, evaluatorId2);

        // Assert
        assertEquals(draft, result);
        assertNotNull(draft.getEvaluation());
        assertEquals(evaluatorId1, draft.getEvaluation().getEvaluatorId());
        assertNotNull(draft.getEvaluation2());
        assertEquals(evaluatorId2, draft.getEvaluation2().getEvaluatorId());
        assertEquals(EnumDegreeWorkStateType.DRAFT_JURY_ASSIGNED, draft.getGeneralStatus());

        verify(repository).save(draft);
        verify(publisher).sendToNotificationQueue(any());
        verify(publisher).sendToModifierQueue(any());
    }

    @Test
    void assignedEvaluators_WithSameEvaluatorIds_ShouldThrowException() {
        // Arrange
        Long degreeWorkId = 1L;
        Long evaluatorId = 100L;

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            draftService.assignedEvaluators(degreeWorkId, evaluatorId, evaluatorId);
        });

        assertEquals(EnumTypeExceptions.IDENTICAL_EVALUATORS_IDS, exception.getType());
        verify(repository, never()).findUnassignedDraft(any());
    }
}