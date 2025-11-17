package Evaluation_AssignmentService.Patterns;


import Evaluation_AssignmentService.Builders.DraftBuilder;
import Evaluation_AssignmentService.Builders.FormatABuilder;
import Evaluation_AssignmentService.Dto.DraftDTO;
import Evaluation_AssignmentService.Dto.FormatADTO;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.SecurityComponent.EnumTypeExceptions;
import Evaluation_AssignmentService.SecurityComponent.ProcessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessBuilderTest {
    @Test
    void testDraftBuilderBuildFromDTO() {
        DraftDTO dto = new DraftDTO();
        dto.setDegreeWorkId(10L);
        dto.setUrl("http://example.com/draft.pdf");

        DraftBuilder builder = new DraftBuilder();
        Draft result = builder.buildFromDTO(dto);

        assertNotNull(result);
        assertEquals(10L, result.getDegreeworkId());
        assertEquals("http://example.com/draft.pdf", result.getUrl());
        assertEquals(0, result.getDaysPassed()); // por defecto
        assertEquals(EnumProcessStatus.PENDING, result.getStatus());
    }

    @Test
    void testFormatABuilderBuildFromDTO_ValidData() {
        FormatADTO dto = new FormatADTO();
        dto.setDegreeWorkId(5L);
        dto.setUrl("http://example.com/formatA.pdf");
        dto.setCompanyLetterPath("/letters/companyA.pdf");

        FormatABuilder builder = new FormatABuilder();
        FormatA result = builder.buildFromDTO(dto);

        assertNotNull(result);
        assertEquals(5L, result.getDegreeworkId());
        assertEquals("http://example.com/formatA.pdf", result.getUrl());
        assertEquals("/letters/companyA.pdf", result.getCompanyLetterPath());
        assertEquals(EnumProcessStatus.PENDING, result.getStatus());
    }

    @Test
    void testFormatABuilderBuildFromDTO_NullDegreeWorkIdThrowsException() {
        FormatADTO dto = new FormatADTO();
        dto.setUrl("http://example.com/formatA.pdf");
        dto.setCompanyLetterPath("/letters/companyA.pdf");

        FormatABuilder builder = new FormatABuilder();

        ProcessException exception = assertThrows(ProcessException.class, () -> {
            builder.buildFromDTO(dto);
        });

        assertEquals(EnumTypeExceptions.NULL_PARAMETER, exception.getType());
    }
}
