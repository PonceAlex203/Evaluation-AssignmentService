package Evaluation_AssignmentService.Patterns;

import Evaluation_AssignmentService.Builders.DraftBuilder;
import Evaluation_AssignmentService.Builders.FormatABuilder;
import Evaluation_AssignmentService.Dto.DraftDTO;
import Evaluation_AssignmentService.Dto.FormatADTO;
import Evaluation_AssignmentService.Dto.ProcessDTO;
import Evaluation_AssignmentService.Enum.EnumTypeProcess;
import Evaluation_AssignmentService.ProcessEntity.BaseProcess;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessFactoryTest {

    private ProcessFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProcessFactory(new FormatABuilder(), new DraftBuilder());
    }

    @Test
    void testCreateProcessFromDTO_WithFormatADTO() {
        FormatADTO dto = new FormatADTO();
        dto.setDegreeWorkId(1L);
        dto.setUrl("http://url/formatA");
        dto.setCompanyLetterPath("/company/letter");

        BaseProcess result = factory.createProcessFromDTO(dto);

        assertTrue(result instanceof FormatA);
        assertEquals(1L, result.getDegreeworkId());
        assertEquals("http://url/formatA", result.getUrl());
    }

    @Test
    void testCreateProcessFromDTO_WithDraftDTO() {
        DraftDTO dto = new DraftDTO();
        dto.setDegreeWorkId(2L);
        dto.setUrl("http://url/draft");

        BaseProcess result = factory.createProcessFromDTO(dto);

        assertTrue(result instanceof Draft);
        assertEquals(2L, result.getDegreeworkId());
    }

    @Test
    void testCreateProcessFromDTO_UnsupportedDTOThrowsException() {
        ProcessDTO dto = new ProcessDTO() {};

        ProcessException exception = assertThrows(ProcessException.class, () -> {
            factory.createProcessFromDTO(dto);
        });

        assertEquals(EnumTypeExceptions.PROCESS_NOT_SUPPORTED, exception.getType());
    }

    @Test
    void testCreateProcess_WithEnumTypeDraft() {
        BaseProcess result = factory.createProcess(EnumTypeProcess.DRAFT, 100L);

        assertTrue(result instanceof Draft);
        assertEquals(100L, result.getDegreeworkId());
    }

    @Test
    void testCreateProcess_WithEnumTypeFormatA() {
        BaseProcess result = factory.createProcess(EnumTypeProcess.FORMAT_A, 50L);

        assertTrue(result instanceof FormatA);
        assertEquals(50L, result.getDegreeworkId());
    }

    @Test
    void testCreateProcess_UnsupportedTypeThrowsException() {
        ProcessException exception = assertThrows(ProcessException.class, () -> {
            factory.createProcess(null, 1L);
        });
        assertEquals(EnumTypeExceptions.PROCESS_NOT_SUPPORTED, exception.getType());
    }
}
