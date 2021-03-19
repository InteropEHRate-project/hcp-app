package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.impl.HealthCareProfessionalServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/current-patient/current-medications/prescription")
public class MedicationController {
    private final HealthCareProfessionalServiceImpl healthCareProfessionalService;
    private final MedicationService medicationService;

    public MedicationController(HealthCareProfessionalServiceImpl healthCareProfessionalService, MedicationService medicationService) {
        this.healthCareProfessionalService = healthCareProfessionalService;
        this.medicationService = medicationService;
    }

    @GetMapping
    @RequestMapping("/idToMedicationSEHR")
    public String viewSEHRSection(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("prescription", PrescriptionController.prescriptionCommand.find(Long.parseLong(id)));
        model.addAttribute("translation", this.medicationService.getCurrentPatient().getDisplayTranslatedVersion());
        model.addAttribute("doctor", healthCareProfessionalService.getHealthCareProfessional());
        return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_MEDICATION_VIEW;
    }
}
