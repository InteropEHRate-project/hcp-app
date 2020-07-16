package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.SpecimenCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory.SpecimenController;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.SpecimenService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults.SpecimenServiceImpl;
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

class SpecimenControllerTest {
    @Mock
    private Model model;
    private SpecimenController controller;
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
        SpecimenService service = new SpecimenServiceImpl(new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext), terminalFhirContext));
        this.controller = new SpecimenController(service);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.id, this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_SPECIMEN_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("specimen"), any(SpecimenCommand.class));
    }
}