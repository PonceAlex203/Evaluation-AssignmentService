package Evaluation_AssignmentService.Controller;

import Evaluation_AssignmentService.ProcessEvaluation.Dto.DraftDTO;
import Evaluation_AssignmentService.ProcessEvaluation.Dto.FormatADTO;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessController.ProcessController;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.Draft;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessEntity.FormatA;
import Evaluation_AssignmentService.ProcessEvaluation.ProcessService.ProcessFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessControllerTest {
    @Mock
    private ProcessFacade processFacade;

    @InjectMocks
    private ProcessController controller;

    private Draft draft;
    private FormatA formatA;

    @BeforeEach
    void setUp() {
        draft = new Draft(1L, "url-draft");
        controller = new ProcessController(processFacade);
    }

    //Draft
    @Test
    void testGetDraftByIdFound() {
        when(processFacade.findDraftByDegreeWorkId(1L)).thenReturn(draft);
        ResponseEntity<Draft> response = controller.getDraftById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(draft, response.getBody());
    }

    @Test
    void testGetDraftByIdNotFound() {
        when(processFacade.findDraftByDegreeWorkId(1L)).thenReturn(null);
        ResponseEntity<Draft> response = controller.getDraftById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSaveDraft() {
        DraftDTO dto = new DraftDTO();
        when(processFacade.saveDraft(dto)).thenReturn(draft);

        ResponseEntity<Draft> response = controller.saveDraft(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(draft, response.getBody());
    }

    @Test
    void testGetAllDrafts() {
        when(processFacade.getAllDrafts()).thenReturn(List.of(draft));
        ResponseEntity<List<Draft>> response = controller.getAllDrafts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    //FormatA
    @Test
    void testGetFormatAByIdFound() {
        when(processFacade.getFormatAByDegreeWorkId(1L)).thenReturn(formatA);
        ResponseEntity<FormatA> response = controller.getFormatAById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(formatA, response.getBody());
    }

    @Test
    void testGetFormatAByIdNotFound() {
        when(processFacade.getFormatAByDegreeWorkId(1L)).thenReturn(null);
        ResponseEntity<FormatA> response = controller.getFormatAById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSaveFormatA() {
        FormatADTO dto = new FormatADTO();
        when(processFacade.saveFormatA(dto)).thenReturn(formatA);

        ResponseEntity<FormatA> response = controller.saveFormatA(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(formatA, response.getBody());
    }
}
