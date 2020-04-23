package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.laboratoryresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ResultCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ResultService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults.ResultServiceImpl;
import eu.interopehrate.ihs.terminalclient.services.impl.TranslateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ResultControllerTest {
    @Mock
    private Model model;
    private ResultController controller;
    private String id = "10";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ResultService service = new ResultServiceImpl(new CurrentPatient(new TranslateServiceImpl(new RestTemplate())));
        this.controller = new ResultController(service);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.id, this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_RESULT_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("result"), any(ResultCommand.class));
    }
}