package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.impl.HealthCareProfessionalServiceImpl;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryMedicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/current-patient/medication-summary/prescription")
public class MedicationSummaryMedicationController {
    private final MedicationSummaryMedicationService medicationSummaryMedicationService;
    private final HealthCareProfessionalServiceImpl healthCareProfessionalService;

    public MedicationSummaryMedicationController(MedicationSummaryMedicationService medicationSummaryMedicationService, HealthCareProfessionalServiceImpl healthCareProfessionalService) {
        this.medicationSummaryMedicationService = medicationSummaryMedicationService;
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
                              Model model) {
        model.addAttribute("medicationCommand", medicationSummaryMedicationService.medicationCommand(id, drug, status, notes, timings, drugDosage, dateOfPrescription));
        model.addAttribute("doctor", healthCareProfessionalService.getHealthCareProfessional());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_MEDICATION_VIEW;
    }
}
