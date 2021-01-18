package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandHealthCareProfessional;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareProfessionalCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.MedicationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription.MedicationController;
import eu.interopehrate.hcpapp.services.administration.impl.HealthCareProfessionalServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications.MedicationServiceImpl;
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

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MedicationControllerTest {
    @Mock
    private Model model;
    private MedicationController controller;
    private String id = "10";
    private String drug = "Data test";
    private String status = "active";
    private String notes = "take with food";
    private String timings = "Frequency: 1, Period: 2, PeriodUnit: D";
    private String drugDosage = "2 tablet per day";
    private LocalDate start = LocalDate.now();
    private LocalDate end = LocalDate.now();
    @Mock
    private HealthCareProfessionalRepository healthCareProfessionalRepository;
    @Mock
    private MachineTranslateService machineTranslateService;
    @Mock
    private ConceptTranslateService conceptTranslateService;
    @Mock
    private ExtendWithTranslationService extendWithTranslationService;
    @Mock
    private TerminalFhirContext terminalFhirContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MedicationService service = new MedicationServiceImpl(new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService, this.extendWithTranslationService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext), terminalFhirContext));
        HealthCareProfessionalServiceImpl healthCareProfessionalService = new HealthCareProfessionalServiceImpl(healthCareProfessionalRepository, new EntityToCommandHealthCareProfessional());
        this.controller = new MedicationController(service, healthCareProfessionalService);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.id, this.drug, this.status, this.notes, this.timings, this.drugDosage, LocalDate.now(), this.start, this.end, this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_MEDICATION_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("medicationCommand"), any(MedicationCommand.class));
        verify(this.model, times(1)).addAttribute(eq("doctor"), any(HealthCareProfessionalCommand.class));
    }
}
