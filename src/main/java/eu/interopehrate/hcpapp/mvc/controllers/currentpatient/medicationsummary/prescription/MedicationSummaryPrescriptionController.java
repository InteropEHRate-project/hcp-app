package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/current-patient/medication-summary/prescription/view-section")
public class MedicationSummaryPrescriptionController {
    private final MedicationSummaryPrescriptionService medicationSummaryPrescriptionService;

    public MedicationSummaryPrescriptionController(MedicationSummaryPrescriptionService medicationSummaryPrescriptionService) {
        this.medicationSummaryPrescriptionService = medicationSummaryPrescriptionService;
    }

    @GetMapping
    @RequestMapping("/idToPrescription")
    public String viewSection(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("prescriptionCommand", medicationSummaryPrescriptionService.prescriptionCommand(id));
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_VIEW_SECTION;
    }
}
