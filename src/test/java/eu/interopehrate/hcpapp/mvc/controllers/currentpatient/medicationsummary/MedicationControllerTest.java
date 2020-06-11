package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryMedicationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription.MedicationSummaryMedicationController;
import eu.interopehrate.hcpapp.services.administration.impl.HealthCareProfessionalServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary.MedicationSummaryMedicationServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryMedicationService;
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
    private MedicationSummaryMedicationController controller;
    private String id = "10";
    private String drug = "Data test";
    private String status = "active";
    private String notes = "take with food";
    private String timings = "Frequency: 1, Period: 2, PeriodUnit: D";
    private String drugDosage = "2 tablet per day";
    @Mock
    private MedicationSummaryMedicationServiceImpl medicationSummaryMedicationService;
    @Mock
    private HealthCareProfessionalServiceImpl healthCareProfessionalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MedicationSummaryMedicationService service = new MedicationSummaryMedicationServiceImpl(new CurrentPatient(new TranslateServiceImpl(new RestTemplate())));
        this.controller = new MedicationSummaryMedicationController(medicationSummaryMedicationService, healthCareProfessionalService);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.id, this.drug, this.status, this.notes, this.timings, this.drugDosage, LocalDate.now(), this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_MEDICATION_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("medicationCommand"), any(MedicationSummaryMedicationCommand.class));
    }
}
