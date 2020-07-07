package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.impl.HealthCareProfessionalServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/current-patient/current-medications/prescription")
public class MedicationController {
    private final MedicationService medicationService;
    private final HealthCareProfessionalServiceImpl healthCareProfessionalService;

    public MedicationController(MedicationService medicationService, HealthCareProfessionalServiceImpl healthCareProfessionalService) {
        this.medicationService = medicationService;
        this.healthCareProfessionalService = healthCareProfessionalService;
    }

    @GetMapping
    @RequestMapping("/idToMedication")
    public String viewSection(@RequestParam(name = "id") String id,
                              @RequestParam(name = "drug") String drug,
                              @RequestParam(name = "status") String status,
                              @RequestParam(name = "notes") String notes,
                              @RequestParam(name = "timings") String timings,
                              @RequestParam(name = "drugDosage") String drugDosage,
                              @RequestParam(name = "dateOfPrescription") LocalDate dateOfPrescription,
                              @RequestParam(name = "start") LocalDate start,
                              @RequestParam(name = "end") LocalDate end,
                              Model model) {
        model.addAttribute("medicationCommand", medicationService.medicationCommand(id, drug, status, notes, timings, drugDosage, dateOfPrescription, start, end));
        model.addAttribute("doctor", healthCareProfessionalService.getHealthCareProfessional());
        return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_MEDICATION_VIEW;
    }
}
