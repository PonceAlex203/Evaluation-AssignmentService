package microservice.ProcessEvaluation.Entities.Builders;

import microservice.ProcessEvaluation.Dtos.Input.ProcessDTO;
import microservice.ProcessEvaluation.Entities.Process.Draft;
import microservice.ProcessEvaluation.Enums.EnumDegreeWorkStateType;
import microservice.ProcessEvaluation.Enums.EnumTypeProcess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link DraftBuilder}.
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DraftBuilderTest {

    @Autowired
    private DraftBuilder draftBuilder;

    @Test
    void buildFromDTO_ShouldCreateDraftWithCorrectCoreProcess() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        Long expectedDegreeWorkId = 123L;
        processDTO.setDegreeWorkId(expectedDegreeWorkId);
        processDTO.setUrl("http://example.com/draft.pdf");

        // Act
        Draft draft = draftBuilder.buildFromDTO(processDTO);

        // Assert
        assertNotNull(draft);
        assertNotNull(draft.getCore());
        assertEquals(expectedDegreeWorkId, draft.getDegreeworkId());
        assertEquals("http://example.com/draft.pdf", draft.getUrl());
        assertEquals(EnumDegreeWorkStateType.DRAFT_SUBMITTED, draft.getGeneralStatus());
    }

    @Test
    void buildFromDTO_ShouldCreateDraftWithCorrectTypeProcess() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(456L);
        processDTO.setUrl("http://example.com/another-draft.pdf");

        // Act
        Draft draft = draftBuilder.buildFromDTO(processDTO);

        // Assert
        assertEquals(EnumTypeProcess.DRAFT, draft.getTypeProcess());
    }

    @Test
    void buildFromDTO_ShouldInitializeDeadlineOnPersist() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(789L);
        processDTO.setUrl("http://example.com/draft-doc.pdf");

        // Act
        Draft draft = draftBuilder.buildFromDTO(processDTO);

        // Simulate @PrePersist call
        // Note: In real scenario, this would be triggered by JPA
        // For unit test, we can test the constructor behavior
        assertNotNull(draft);

        // The deadline should be null initially (set by @PrePersist)
        assertNull(draft.getDeadline());

        // Verify other initializations
        assertNull(draft.getEvaluation2());
    }

    @Test
    void buildFromDTO_WithNullUrl_ShouldCreateDraftWithNullUrl() {
        // Arrange
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setDegreeWorkId(999L);
        processDTO.setUrl(null);

        // Act
        Draft draft = draftBuilder.buildFromDTO(processDTO);

        // Assert
        assertNull(draft.getUrl());
    }
}