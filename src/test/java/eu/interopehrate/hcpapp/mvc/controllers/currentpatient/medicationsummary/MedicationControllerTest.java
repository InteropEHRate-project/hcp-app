package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandHealthCareProfessional;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareProfessionalCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription.MedicationController;
import eu.interopehrate.hcpapp.services.administration.impl.HealthCareProfessionalServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MedicationControllerTest {
    @Mock
    private Model model;
    private MedicationController controller;
    @Mock
    private HealthCareProfessionalRepository healthCareProfessionalRepository;
    @Mock
    private MedicationService medicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        HealthCareProfessionalServiceImpl healthCareProfessionalService = new HealthCareProfessionalServiceImpl(healthCareProfessionalRepository, new EntityToCommandHealthCareProfessional());
        this.controller = new MedicationController(healthCareProfessionalService, medicationService);
    }

    @Test
    void viewSection() {
        String returnedString = this.controller.viewSection("10", this.model);
        Assertions.assertEquals(TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_MEDICATION_VIEW, returnedString);
        verify(this.model, times(1)).addAttribute(eq("doctor"), any(HealthCareProfessionalCommand.class));
    }
}
