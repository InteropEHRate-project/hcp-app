package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandPrescriptionTranslate;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription.PrescriptionController;
import eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary.PrescriptionServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.PrescriptionService;
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

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PrescriptionControllerTest {
    @Mock
    private Model model;
    private PrescriptionController controller;
    @Mock
    private HapiToCommandPrescription hapiToCommandPrescription;
    @Mock
    private HapiToCommandPrescriptionTranslate hapiToCommandPrescriptionTranslate;
    @Mock
    private CurrentD2DConnection currentD2DConnection;
    @Mock
    private MachineTranslateService machineTranslateService;
    @Mock
    private ConceptTranslateService conceptTranslateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        PrescriptionService service = new PrescriptionServiceImpl
                (new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService), new CodesConversionServiceImpl(new RestTemplate())),
                        hapiToCommandPrescription, hapiToCommandPrescriptionTranslate, currentD2DConnection);
        this.controller = new PrescriptionController(service);
    }

    @Test
    void viewSection() throws IOException {
        String returnedString = this.controller.viewSection(this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_VIEW_SECTION, returnedString);
        verify(this.model, times(1)).addAttribute(eq("prescriptionCommand"), any(PrescriptionCommand.class));
    }
}
