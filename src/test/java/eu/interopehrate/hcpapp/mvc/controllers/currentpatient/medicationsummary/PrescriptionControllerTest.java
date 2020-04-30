package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription.MedicationSummaryPrescriptionController;
import eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary.MedicationSummaryPrescriptionServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
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

public class PrescriptionControllerTest {
    @Mock
    private Model model;
    private MedicationSummaryPrescriptionController controller;
    private String id = "10";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MedicationSummaryPrescriptionService service = new MedicationSummaryPrescriptionServiceImpl(new CurrentPatient(new TranslateServiceImpl(new RestTemplate())));
        this.controller = new MedicationSummaryPrescriptionController(service);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection(this.id, this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_VIEW_SECTION, returnedString);
        verify(this.model, times(1)).addAttribute(eq("prescriptionCommand"), any(MedicationSummaryPrescriptionCommand.class));
    }
}
