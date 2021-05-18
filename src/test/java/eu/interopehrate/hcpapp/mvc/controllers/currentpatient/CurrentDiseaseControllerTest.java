package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityCurrentDisease;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandCurrentDisease;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandCurrentDisease;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.CurrentDiseaseServiceImpl;
import eu.interopehrate.ihs.terminalclient.fhir.TerminalFhirContext;
import eu.interopehrate.ihs.terminalclient.services.ConceptTranslateService;
import eu.interopehrate.ihs.terminalclient.services.ExtendWithTranslationService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CurrentDiseaseControllerTest {
    @Mock
    private Model model;
    private CurrentDiseaseController controller;
    @Mock
    private MachineTranslateService machineTranslateService;
    @Mock
    private ConceptTranslateService conceptTranslateService;
    @Mock
    private ExtendWithTranslationService extendWithTranslationService;
    @Mock
    private HapiToCommandCurrentDisease hapiToCommandCurrentDisease;
    @Mock
    private CommandToEntityCurrentDisease commandToEntityCurrentDisease;
    @Mock
    private CurrentDiseaseRepository currentDiseaseRepository;
    @Mock
    private EntityToCommandCurrentDisease entityToCommandCurrentDisease;
    @Mock
    private TerminalFhirContext terminalFhirContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        CurrentDiseaseService service = new CurrentDiseaseServiceImpl(
                new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService, this.extendWithTranslationService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext), terminalFhirContext),
                hapiToCommandCurrentDisease, commandToEntityCurrentDisease, currentDiseaseRepository, entityToCommandCurrentDisease);
        this.controller = new CurrentDiseaseController(service);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_VIEW_SECTION, returnedString);
        verify(this.model, times(1)).addAttribute(eq("currentDiseaseCommand"), any(CurrentDiseaseCommand.class));
    }
}