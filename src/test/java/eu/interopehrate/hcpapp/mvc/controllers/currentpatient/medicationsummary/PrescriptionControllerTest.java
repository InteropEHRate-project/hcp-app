package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescriptionTranslate;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription.PrescriptionController;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications.PrescriptionServiceImpl;
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
    @Mock
    private TerminalFhirContext terminalFhirContext;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        PrescriptionService service = new PrescriptionServiceImpl
                (new CurrentPatient(new TranslateServiceImpl(this.conceptTranslateService, this.machineTranslateService), new CodesConversionServiceImpl(new RestTemplate(), terminalFhirContext)),
                        hapiToCommandPrescription, hapiToCommandPrescriptionTranslate, currentD2DConnection);
        this.controller = new PrescriptionController(service);
    }

    @Test
    void viewSection() throws IOException {
        String returnedString = this.controller.viewSection(this.model);
        assertEquals(TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_VIEW_SECTION, returnedString);
        verify(this.model, times(1)).addAttribute(eq("prescriptionCommand"), any(PrescriptionCommand.class));
    }
}
