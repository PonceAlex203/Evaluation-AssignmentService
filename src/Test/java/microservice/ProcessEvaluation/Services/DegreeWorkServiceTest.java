package microservice.ProcessEvaluation.Services;

import microservice.ProcessEvaluation.Repositories.DegreeWorkRepository;
import microservice.SecurityComponent.DegreeWorkIds;
import microservice.SecurityComponent.EnumTypeExceptions;
import microservice.SecurityComponent.ProcessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link DegreeWorkService}.
 */
@ExtendWith(MockitoExtension.class)
class DegreeWorkServiceTest {

    @Mock
    private DegreeWorkRepository degreeWorkRepository;

    private DegreeWorkService degreeWorkService;

    @BeforeEach
    void setUp() {
        degreeWorkService = new DegreeWorkService(degreeWorkRepository);
    }

    @Test
    void constructor_ShouldInitializeWithRepository() {
        // Arrange & Act already done in setUp()

        // Assert
        assertNotNull(degreeWorkService);
        assertNotNull(degreeWorkRepository);
    }

    @Test
    void saveDegreeWorkId_WhenIdDoesNotExist_ShouldSaveSuccessfully() {
        // Arrange
        Long degreeWorkId = 100L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(false);

        // Act
        degreeWorkService.saveDegreeWorkId(degreeWorkId);

        // Assert
        verify(degreeWorkRepository).existsById(degreeWorkId);
        verify(degreeWorkRepository).save(argThat(degreeWorkIds ->
                degreeWorkIds.getId().equals(degreeWorkId)
        ));
    }

    @Test
    void saveDegreeWorkId_WhenIdAlreadyExists_ShouldThrowException() {
        // Arrange
        Long degreeWorkId = 200L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(true);

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            degreeWorkService.saveDegreeWorkId(degreeWorkId);
        });

        assertEquals(EnumTypeExceptions.DEGREEWORKID_EXISTING, exception.getType());
        verify(degreeWorkRepository).existsById(degreeWorkId);
        verify(degreeWorkRepository, never()).save(any());
    }

    @Test
    void saveDegreeWorkId_WithNullId_ShouldThrowException() {
        // Arrange
        when(degreeWorkRepository.existsById(null)).thenThrow(IllegalArgumentException.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            degreeWorkService.saveDegreeWorkId(null);
        });
    }

    @Test
    void validateExistingId_WhenIdExists_ShouldNotThrowException() {
        // Arrange
        Long degreeWorkId = 300L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            degreeWorkService.validateExistingId(degreeWorkId);
        });

        verify(degreeWorkRepository).existsById(degreeWorkId);
    }

    @Test
    void validateExistingId_WhenIdDoesNotExist_ShouldThrowException() {
        // Arrange
        Long degreeWorkId = 400L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(false);

        // Act & Assert
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            degreeWorkService.validateExistingId(degreeWorkId);
        });

        assertEquals(EnumTypeExceptions.DEGREEWORKID_NOT_FOUND, exception.getType());
        verify(degreeWorkRepository).existsById(degreeWorkId);
    }

    @Test
    void validateExistingId_WithNullId_ShouldThrowException() {
        // Arrange
        when(degreeWorkRepository.existsById(null)).thenThrow(IllegalArgumentException.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            degreeWorkService.validateExistingId(null);
        });
    }

    @Test
    void existsId_WhenIdExists_ShouldReturnTrue() {
        // Arrange
        Long degreeWorkId = 500L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(true);

        // Act
        boolean result = degreeWorkService.existsId(degreeWorkId);

        // Assert
        assertTrue(result);
        verify(degreeWorkRepository).existsById(degreeWorkId);
    }

    @Test
    void existsId_WhenIdDoesNotExist_ShouldReturnFalse() {
        // Arrange
        Long degreeWorkId = 600L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(false);

        // Act
        boolean result = degreeWorkService.existsId(degreeWorkId);

        // Assert
        assertFalse(result);
        verify(degreeWorkRepository).existsById(degreeWorkId);
    }

    @Test
    void existsId_WithNullId_ShouldReturnFalse() {
        // Arrange
        when(degreeWorkRepository.existsById(null)).thenReturn(false);

        // Act
        boolean result = degreeWorkService.existsId(null);

        // Assert
        assertFalse(result);
        verify(degreeWorkRepository).existsById(null);
    }

    @Test
    void initializr_ShouldSavePredefinedIds() {
        // Arrange
        // We need to test a private method, so we'll use reflection
        // Alternatively, we could make the method package-private for testing

        // Act - Call the private method using reflection
        try {
            var method = DegreeWorkService.class.getDeclaredMethod("initializr");
            method.setAccessible(true);
            method.invoke(degreeWorkService);
        } catch (Exception e) {
            fail("Failed to invoke initializr method: " + e.getMessage());
        }

        // Assert
        verify(degreeWorkRepository, times(3)).save(any(DegreeWorkIds.class));
        verify(degreeWorkRepository).save(argThat(ids -> ids.getId().equals(1L)));
        verify(degreeWorkRepository).save(argThat(ids -> ids.getId().equals(2L)));
        verify(degreeWorkRepository).save(argThat(ids -> ids.getId().equals(3L)));
    }

    @Test
    void saveDegreeWorkId_ShouldCreateDegreeWorkIdsWithCorrectId() {
        // Arrange
        Long degreeWorkId = 999L;
        when(degreeWorkRepository.existsById(degreeWorkId)).thenReturn(false);

        // Capture the argument passed to save()
        final DegreeWorkIds[] capturedIds = new DegreeWorkIds[1];
        doAnswer(invocation -> {
            capturedIds[0] = invocation.getArgument(0);
            return null;
        }).when(degreeWorkRepository).save(any(DegreeWorkIds.class));

        // Act
        degreeWorkService.saveDegreeWorkId(degreeWorkId);

        // Assert
        assertNotNull(capturedIds[0]);
        assertEquals(degreeWorkId, capturedIds[0].getId());
    }
}