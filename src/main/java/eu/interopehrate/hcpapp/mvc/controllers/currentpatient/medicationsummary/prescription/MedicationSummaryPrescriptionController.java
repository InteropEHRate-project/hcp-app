package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/medication-summary/prescription")
public class MedicationSummaryPrescriptionController {
    private final MedicationSummaryPrescriptionService medicationSummaryPrescriptionService;

    public MedicationSummaryPrescriptionController(MedicationSummaryPrescriptionService medicationSummaryPrescriptionService) {
        this.medicationSummaryPrescriptionService = medicationSummaryPrescriptionService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("prescriptionCommand", medicationSummaryPrescriptionService.prescriptionCommand());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_VIEW_SECTION;
    }
}
