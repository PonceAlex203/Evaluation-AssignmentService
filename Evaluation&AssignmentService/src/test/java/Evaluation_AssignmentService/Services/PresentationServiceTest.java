package Evaluation_AssignmentService.Services;

import Evaluation_AssignmentService.ProcessEntity.Presentation;
import Evaluation_AssignmentService.ProcessRepository.PresentationRepository;
import Evaluation_AssignmentService.ProcessService.PresentationService;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PresentationServiceTest {
    @Mock
    private PresentationRepository presentationRepository;

    @InjectMocks
    private PresentationService presentationService;

    private Presentation validPresentation;

    @BeforeEach
    void setUp() {
        validPresentation = new Presentation(1L, List.of(10L, 20L));
    }

    @Test
    void testFindByIdReturnsPresentation() {
        when(presentationRepository.findById(1L)).thenReturn(Optional.of(validPresentation));

        Presentation result = presentationService.findById(1L);

        assertNotNull(result);
        assertEquals(validPresentation, result);
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        when(presentationRepository.findById(1L)).thenReturn(Optional.empty());

        Presentation result = presentationService.findById(1L);

        assertNull(result);
    }

    @Test
    void testSavePresentationSuccess() {
        when(presentationRepository.existsById(1L)).thenReturn(false);
        when(presentationRepository.save(validPresentation)).thenReturn(validPresentation);

        Presentation result = presentationService.save(validPresentation);

        assertNotNull(result);
        verify(presentationRepository, times(1)).save(validPresentation);
    }

    @Test
    void testSaveThrowsExceptionWhenAlreadyExists() {
        when(presentationRepository.existsById(1L)).thenReturn(true);

        assertThrows(ProcessException.class, () -> presentationService.save(validPresentation));
    }

    @Test
    void testSaveThrowsExceptionWhenNull() {
        assertThrows(ProcessException.class, () -> presentationService.save(null));
    }

    @Test
    void testSaveThrowsExceptionWhenJuryListEmpty() {
        Presentation invalid = new Presentation(1L, List.of());
        assertThrows(ProcessException.class, () -> presentationService.save(invalid));
    }

    @Test
    void testSaveThrowsExceptionWhenMoreThanTwoJurys() {
        Presentation invalid = new Presentation(1L, List.of(1L, 2L, 3L));
        assertThrows(ProcessException.class, () -> presentationService.save(invalid));
    }

    @Test
    void testSaveThrowsExceptionWhenJurysAreEqual() {
        Presentation invalid = new Presentation(1L, List.of(10L, 10L));
        assertThrows(ProcessException.class, () -> presentationService.save(invalid));
    }

    @Test
    void testUpdateSuccess() {
        Presentation newPresentation = new Presentation(1L, List.of(30L, 40L));

        when(presentationRepository.findById(1L)).thenReturn(Optional.of(validPresentation));
        when(presentationRepository.save(newPresentation)).thenReturn(newPresentation);

        Presentation result = presentationService.update(1L, newPresentation);

        assertNotNull(result);
        verify(presentationRepository, times(1)).save(newPresentation);
    }

    @Test
    void testUpdateThrowsExceptionWhenNotFound() {
        when(presentationRepository.findById(1L)).thenReturn(Optional.empty());
        Presentation newPresentation = new Presentation(1L, List.of(10L, 20L));

        assertThrows(ProcessException.class, () -> presentationService.update(1L, newPresentation));
    }

    @Test
    void testUpdateThrowsExceptionWhenInvalidPresentation() {
        Presentation invalid = new Presentation(1L, List.of(1L, 1L));
        when(presentationRepository.findById(1L)).thenReturn(Optional.of(validPresentation));

        assertThrows(ProcessException.class, () -> presentationService.update(1L, invalid));
    }
}
