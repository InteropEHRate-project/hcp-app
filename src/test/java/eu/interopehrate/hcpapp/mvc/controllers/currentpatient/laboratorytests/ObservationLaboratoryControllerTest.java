package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.converters.fhir.laboratorytests.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.observationlaboratory.ObservationLaboratoryController;
import eu.interopehrate.hcpapp.services.currentpatient.impl.laboratorytests.ObservationLaboratoryServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
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

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class ObservationLaboratoryControllerTest {
    @Mock
    private Model model;
    private ObservationLaboratoryController controller;
    @Mock
    private MachineTranslateService machineTranslateService;
    @Mock
    private ConceptTranslateService conceptTranslateService;
    @Mock
    private TerminalFhirContext terminalFhirContext;
    @Mock
    private HttpSession httpSession;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ObservationLaboratoryService service = new ObservationLaboratoryServiceImpl(new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext), terminalFhirContext), new HapiToCommandObservationLaboratory(new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext), terminalFhirContext)));
        this.controller = new ObservationLaboratoryController(service);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.model, this.httpSession, "");
        assertEquals(TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("labResultsAnalysisCommand"), any(ObservationLaboratoryCommandAnalysis.class));
    }
}