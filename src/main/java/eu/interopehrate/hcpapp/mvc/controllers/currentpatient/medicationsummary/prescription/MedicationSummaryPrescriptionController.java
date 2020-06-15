package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/current-patient/medication-summary/prescription")
public class MedicationSummaryPrescriptionController {
    private final MedicationSummaryPrescriptionService medicationSummaryPrescriptionService;

    public MedicationSummaryPrescriptionController(MedicationSummaryPrescriptionService medicationSummaryPrescriptionService) {
        this.medicationSummaryPrescriptionService = medicationSummaryPrescriptionService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) throws IOException {
        model.addAttribute("prescriptionCommand", medicationSummaryPrescriptionService.prescriptionCommand());
        model.addAttribute("prescriptionCommandUpload", medicationSummaryPrescriptionService.prescriptionCommandUpload());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_PRESCRIPTION_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("medicationSummaryPrescriptionInfoCommand", new MedicationSummaryPrescriptionInfoCommand());
        return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_ADD_PAGE;
        }
        medicationSummaryPrescriptionService.insertPrescription(medicationSummaryPrescriptionInfoCommand);
        return "redirect:/current-patient/medication-summary/prescription/view-section";
    }
}
