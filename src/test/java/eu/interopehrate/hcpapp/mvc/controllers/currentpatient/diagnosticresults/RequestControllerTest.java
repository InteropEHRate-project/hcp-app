package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.RequestCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory.RequestController;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.RequestService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults.RequestServiceImpl;
import eu.interopehrate.ihs.terminalclient.fhir.TerminalFhirContext;
import eu.interopehrate.ihs.terminalclient.services.ConceptTranslateService;
import eu.interopehrate.ihs.terminalclient.services.MachineTranslateService;
import eu.interopehrate.ihs.terminalclient.services.impl.CodesConversionServiceImpl;
import eu.interopehrate.ihs.terminalclient.services.impl.TranslateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class RequestControllerTest {
    @Mock
    private Model model;
    private RequestController controller;
    private String id = "10";
    @Mock
    private MachineTranslateService machineTranslateService;
    @Mock
    private ConceptTranslateService conceptTranslateService;
    @Mock
    private TerminalFhirContext terminalFhirContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        RequestService service = new RequestServiceImpl(new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext), terminalFhirContext));
        this.controller = new RequestController(service);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.id, this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_REQUEST_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("request"), any(RequestCommand.class));
    }
}