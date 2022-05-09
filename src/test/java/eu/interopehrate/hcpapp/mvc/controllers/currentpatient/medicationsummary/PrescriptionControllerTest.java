package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPrescription;
import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription.PrescriptionController;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications.PrescriptionServiceImpl;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PrescriptionControllerTest {
    @Mock
    private Model model;
    private PrescriptionController controller;
    @Mock
    private HapiToCommandPrescription hapiToCommandPrescription;
    @Mock
    private CurrentD2DConnection currentD2DConnection;
    @Mock
    private HttpSession session;
    @Mock
    private CurrentPatient currentPatient;
    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private AuditInformationService auditInformationService;
    @Mock
    private PrescriptionTypesRepository prescriptionTypesRepository;
    @Mock
    private CloudConnection cloudConnection;
    @Mock
    private TranslateService translateService;
    private CommandToEntityPrescription commandToEntityPrescription;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        PrescriptionService service =
                new PrescriptionServiceImpl(this.currentPatient, this.hapiToCommandPrescription, commandToEntityPrescription, this.prescriptionRepository, this.currentD2DConnection,
                        auditInformationService, prescriptionTypesRepository, this.cloudConnection, translateService);
        this.controller = new PrescriptionController(service);
    }

    @Test
    void viewSection() throws IOException {
        String returnedString = this.controller.viewSection(this.model, this.session, "");
        assertEquals(TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_VIEW_SECTION, returnedString);
        verify(this.model, times(1)).addAttribute(eq("prescriptionCommand"), any(PrescriptionCommand.class));
    }
}
