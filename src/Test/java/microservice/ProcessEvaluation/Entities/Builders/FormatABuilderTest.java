package microservice.ProcessEvaluation.Entities.Builders;

import microservice.ProcessEvaluation.Entities.Process.FormatA;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;

/**
 * Unit tests for {@link FormatABuilder}.
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FormatABuilderTest {

    @Autowired
    private FormatABuilder formatABuilder;

    @Test
    void buildFromDTO_ShouldCreateFormatAWithCorrectCoreProcess() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        Long expectedDegreeWorkId = 100L;
        processDTO.setDegreeWorkId(expectedDegreeWorkId);
        processDTO.setUrl("http://example.com/format-a.pdf");

        // Act
        FormatA formatA = formatABuilder.buildFromDTO(processDTO);

        // Assert
        assertNotNull(formatA);
        assertNotNull(formatA.getCore());
        assertEquals(expectedDegreeWorkId, formatA.getDegreeworkId());
        assertEquals("http://example.com/format-a.pdf", formatA.getUrl());
        assertEquals(EnumDegreeWorkStateType.FORMAT_A_SUBMITTED, formatA.getGeneralStatus());
    }

    @Test
    void buildFromDTO_ShouldCreateFormatAWithCorrectTypeProcess() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(200L);
        processDTO.setUrl("http://example.com/format-a-doc.pdf");

        // Act
        FormatA formatA = formatABuilder.buildFromDTO(processDTO);

        // Assert
        assertEquals(EnumTypeProcess.FORMAT_A, formatA.getTypeProcess());
    }

    @Test
    void buildFromDTO_ShouldInitializeAttemptsToOne() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(300L);
        processDTO.setUrl("http://example.com/test.pdf");

        // Act
        FormatA formatA = formatABuilder.buildFromDTO(processDTO);

        // Assert
        assertEquals(1, formatA.getAttempts());
    }

    @Test
    void buildFromDTO_ShouldInitializeDefaultEvaluation() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(400L);
        processDTO.setUrl("http://example.com/document.pdf");

        // Act
        FormatA formatA = formatABuilder.buildFromDTO(processDTO);

        // Assert
        assertNotNull(formatA.getEvaluation());
        // Default evaluation should have null evaluatorId
        assertNull(formatA.getEvaluation().getEvaluatorId());
    }

    @Test
    void buildFromDTO_WithNullUrl_ShouldCreateFormatAWithNullUrl() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(500L);
        processDTO.setUrl(null);

        // Act
        FormatA formatA = formatABuilder.buildFromDTO(processDTO);

        // Assert
        assertNull(formatA.getUrl());
    }

    @Test
    void buildFromDTO_ShouldAllowAttemptsIncrement() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(600L);
        processDTO.setUrl("http://example.com/file.pdf");

        // Act
        FormatA formatA = formatABuilder.buildFromDTO(processDTO);
        formatA.increaseAttempts();

        // Assert
        assertEquals(2, formatA.getAttempts());
    }
}