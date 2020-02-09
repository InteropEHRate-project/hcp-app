package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryMedicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/medication-summary/medication")
public class MedicationSummeryMedicationController {
    private final MedicationSummaryMedicationService medicationSummaryMedicationService;

    public MedicationSummeryMedicationController(MedicationSummaryMedicationService medicationSummaryMedicationService) {
        this.medicationSummaryMedicationService = medicationSummaryMedicationService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("medicationCommand", medicationSummaryMedicationService.medicationCommand());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_MEDICATION_VIEW_SECTION;
    }
}
