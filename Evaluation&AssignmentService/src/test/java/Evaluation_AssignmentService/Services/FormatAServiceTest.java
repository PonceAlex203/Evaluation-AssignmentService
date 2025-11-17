package Evaluation_AssignmentService.Services;

import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessRepository.FormatARepository;
import Evaluation_AssignmentService.ProcessService.FormatAService;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FormatAServiceTest {

    @Mock
    private FormatARepository repository;

    @InjectMocks
    private FormatAService formatAService;

    private FormatA formatA;

    @BeforeEach
    void setUp() {
        formatA = new FormatA(1L, "http://url.com", "/company/letter");
        formatA.setStatus(EnumProcessStatus.PENDING);
    }


    @Test
    void testSaveNewFormatA() {
        when(repository.findByDegreeworkId(1L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(formatA);

        FormatA result = formatAService.save(formatA);
        assertNotNull(result);
        verify(repository).save(any(FormatA.class));
    }

    @Test
    void testSaveThrowsWhenAlreadyExists() {
        when(repository.findByDegreeworkId(1L)).thenReturn(Optional.of(formatA));
        assertThrows(ProcessException.class, () -> formatAService.save(formatA));
    }

    @Test
    void testReUploadProcess() {
        FormatA rejected = new FormatA(1L, "url1", "path");
        rejected.setStatus(EnumProcessStatus.REJECTED);

        when(repository.findByDegreeworkId(1L)).thenReturn(Optional.of(rejected));
        when(repository.save(any())).thenReturn(rejected);

        FormatA updated = new FormatA(1L, "newUrl", "path");
        updated.setStatus(EnumProcessStatus.REJECTED);

        FormatA result = formatAService.reUploadProcess(updated);
        assertEquals(EnumProcessStatus.PENDING, result.getStatus());
        verify(repository).save(any(FormatA.class));
    }

    @Test
    void testEvaluateProcess() {
        FormatA pending = new FormatA(1L, "url", "path");
        pending.setStatus(EnumProcessStatus.PENDING);

        when(repository.findByDegreeworkId(1L)).thenReturn(Optional.of(pending));
        when(repository.save(any())).thenReturn(pending);

        FormatA result = formatAService.evaluateProcess(1L, "ok", EnumProcessStatus.APPROVED);
        assertEquals(EnumProcessStatus.APPROVED, result.getStatus());
        verify(repository).save(any(FormatA.class));
    }

    @Test
    void testEvaluateProcessThrowsIfNotPending() {
        formatA.setStatus(EnumProcessStatus.REJECTED);
        when(repository.findByDegreeworkId(1L)).thenReturn(Optional.of(formatA));
        assertThrows(ProcessException.class,
                () -> formatAService.evaluateProcess(1L, "bad", EnumProcessStatus.APPROVED));
    }
}
