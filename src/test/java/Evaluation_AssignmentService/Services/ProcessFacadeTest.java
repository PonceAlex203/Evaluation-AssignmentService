package Evaluation_AssignmentService.Services;

import Evaluation_AssignmentService.Dto.DraftDTO;
import Evaluation_AssignmentService.Dto.EvaluateProcessDTO;
import Evaluation_AssignmentService.Dto.FormatADTO;
import Evaluation_AssignmentService.Enum.EnumProcessStatus;
import Evaluation_AssignmentService.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEntity.ProcessFactory;
import Evaluation_AssignmentService.ProcessService.DraftService;
import Evaluation_AssignmentService.ProcessService.FormatAService;
import Evaluation_AssignmentService.ProcessService.ProcessFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessFacadeTest {
    @Mock
    DraftService draftService;

    @Mock
    FormatAService formatAService;

    @Mock
    ProcessFactory factory;

    @InjectMocks
    ProcessFacade facade;

    //Draft
    @Test
    void testSaveDraft() {
        DraftDTO dto = new DraftDTO();
        Draft draft = new Draft(1L, "url");

        when(factory.createProcessFromDTO(dto)).thenReturn(draft);
        when(draftService.save(draft)).thenReturn(draft);

        Draft result = facade.saveDraft(dto);

        verify(factory).createProcessFromDTO(dto);
        verify(draftService).save(draft);
        assertEquals(draft, result);
    }

    @Test
    void testGetDraftById() {
        Draft draft = new Draft(1L, "url");
        when(draftService.extractByDegreeWorkId(1L)).thenReturn(draft);

        Draft result = facade.findDraftByDegreeWorkId(1L);

        verify(draftService).extractByDegreeWorkId(1L);
        assertEquals(draft, result);
    }

    @Test
    void testGetAllDrafts() {
        List<Draft> drafts = List.of(new Draft(1L, "url"));
        when(draftService.findAll()).thenReturn(drafts);

        List<Draft> result = facade.getAllDrafts();

        verify(draftService).findAll();
        assertEquals(drafts, result);
    }

    @Test
    void testReUploadDraft() {
        DraftDTO dto = new DraftDTO();
        Draft draft = new Draft(1L, "url");

        when(factory.createProcessFromDTO(dto)).thenReturn(draft);
        when(draftService.reUploadProcess(draft)).thenReturn(draft);

        Draft result = facade.reUploadDraft(dto);

        verify(factory).createProcessFromDTO(dto);
        verify(draftService).reUploadProcess(draft);
        assertEquals(draft, result);
    }

    @Test
    void testEvaluateDraft() {
        EvaluateProcessDTO dto = new EvaluateProcessDTO("comment", EnumProcessStatus.APPROVED);
        Draft draft = new Draft(1L, "url");

        when(draftService.evaluateProcess(1L, dto.getComment(), dto.getNewStatus())).thenReturn(draft);

        Draft result = facade.evaluateDraft(1L, dto);

        verify(draftService).evaluateProcess(1L, dto.getComment(), dto.getNewStatus());
        assertEquals(draft, result);
    }

    @Test
    void testGetDraftsByStatus() {
        List<Draft> drafts = List.of(new Draft(1L, "url"));
        when(draftService.findByStatus(EnumProcessStatus.PENDING)).thenReturn(drafts);

        List<Draft> result = facade.getDraftsByStatus(EnumProcessStatus.PENDING);

        verify(draftService).findByStatus(EnumProcessStatus.PENDING);
        assertEquals(drafts, result);
    }

    //FormatA
    @Test
    void testSaveFormatA() {
        FormatADTO dto = new FormatADTO();
        FormatA formatA = new FormatA(1L, "url");

        when(factory.createProcessFromDTO(dto)).thenReturn(formatA);
        when(formatAService.save(formatA)).thenReturn(formatA);

        FormatA result = facade.saveFormatA(dto);

        verify(factory).createProcessFromDTO(dto);
        verify(formatAService).save(formatA);
        assertEquals(formatA, result);
    }

    @Test
    void testGetFormatAById() {
        FormatA formatA = new FormatA(1L, "url");
        when(formatAService.extractByDegreeWorkId(1L)).thenReturn(formatA);

        FormatA result = facade.getFormatAByDegreeWorkId(1L);

        verify(formatAService).extractByDegreeWorkId(1L);
        assertEquals(formatA, result);
    }

    @Test
    void testGetAllFormatAs() {
        List<FormatA> formats = List.of(new FormatA(1L, "url"));
        when(formatAService.findAll()).thenReturn(formats);

        List<FormatA> result = facade.getAllFormatAs();

        verify(formatAService).findAll();
        assertEquals(formats, result);
    }

    @Test
    void testReUploadFormatA() {
        FormatADTO dto = new FormatADTO();
        FormatA formatA = new FormatA(1L, "url");

        when(factory.createProcessFromDTO(dto)).thenReturn(formatA);
        when(formatAService.reUploadProcess(formatA)).thenReturn(formatA);

        FormatA result = facade.reUploadFormatA(dto);

        verify(factory).createProcessFromDTO(dto);
        verify(formatAService).reUploadProcess(formatA);
        assertEquals(formatA, result);
    }

    @Test
    void testEvaluateFormatA() {
        EvaluateProcessDTO dto = new EvaluateProcessDTO("comment", EnumProcessStatus.APPROVED);
        FormatA formatA = new FormatA(1L, "url");

        when(formatAService.evaluateProcess(1L, dto.getComment(), dto.getNewStatus())).thenReturn(formatA);

        FormatA result = facade.evaluateFormatA(1L, dto);

        verify(formatAService).evaluateProcess(1L, dto.getComment(), dto.getNewStatus());
        assertEquals(formatA, result);
    }

    @Test
    void testGetFormatsAByStatus() {
        List<FormatA> formats = List.of(new FormatA(1L, "url"));
        when(formatAService.findByStatus(EnumProcessStatus.PENDING)).thenReturn(formats);

        List<FormatA> result = facade.getFormatsAByStatus(EnumProcessStatus.PENDING);

        verify(formatAService).findByStatus(EnumProcessStatus.PENDING);
        assertEquals(formats, result);
    }
}
