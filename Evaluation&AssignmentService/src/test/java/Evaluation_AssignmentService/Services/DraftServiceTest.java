package Evaluation_AssignmentService.Services;

import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.ProcessRepository.DraftRepository;
import Evaluation_AssignmentService.ProcessService.DraftService;
import Evaluation_AssignmentService.ProcessService.FormatAService;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DraftServiceTest {

    @Mock private DraftRepository draftRepository;
    @Mock private FormatAService formatAService;
    @Mock private EntityManager entityManager;
    @Mock private ProcessFactory processFactory;

    @InjectMocks
    private DraftService draftService;

    private Draft draft;

    @BeforeEach
    void setUp() {
        draft = new Draft(1L, "http://draft.com");
        draft.setStatus(EnumProcessStatus.PENDING);
        ReflectionTestUtils.setField(draftService, "entityManager", entityManager);

    }

    @Test
    void testSaveDraftThrowsIfFormatANotSubmitted() {
        when(formatAService.extractByDegreeWorkId(1L)).thenReturn(null);
        when(draftRepository.findByDegreeworkId(1L)).thenReturn(Optional.empty());

        assertThrows(ProcessException.class, () -> draftService.save(draft));
    }


    @Test
    void testEvaluateDraft() {
        Draft pending = new Draft(1L, "url");
        pending.setStatus(EnumProcessStatus.PENDING);

        when(draftRepository.findByDegreeworkId(1L)).thenReturn(Optional.of(pending));
        when(draftRepository.save(any())).thenReturn(pending);

        Draft result = draftService.evaluateProcess(1L, "ok", EnumProcessStatus.APPROVED);
        assertEquals(EnumProcessStatus.APPROVED, result.getStatus());
        verify(draftRepository).save(any(Draft.class));
    }
}
