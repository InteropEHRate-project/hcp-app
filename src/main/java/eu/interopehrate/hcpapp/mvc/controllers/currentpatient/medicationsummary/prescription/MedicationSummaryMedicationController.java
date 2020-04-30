package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryMedicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/current-patient/medication-summary/prescription/medication-view")
public class MedicationSummaryMedicationController {
    private final MedicationSummaryMedicationService medicationSummaryMedicationService;

    public MedicationSummaryMedicationController(MedicationSummaryMedicationService medicationSummaryMedicationService) {
        this.medicationSummaryMedicationService = medicationSummaryMedicationService;
    }

    @GetMapping
    @RequestMapping("/idToMedication")
    public String viewSection(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("medicationCommand", medicationSummaryMedicationService.medicationCommand(id));
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_MEDICATION_VIEW;
    }
}
