package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary.prescription;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping
    @RequestMapping("/delete")
    public String delete(@RequestParam("drugId") Long drugId) {
        this.medicationSummaryPrescriptionService.deletePrescription(drugId);
        return "redirect:/current-patient/medication-summary/prescription/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openUpdatePage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("medicationSummaryPrescriptionInfoCommand", this.medicationSummaryPrescriptionService.medicationSummaryPrescriptionInfoById(id));
        return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_UPDATE_PAGE;
    }

    @PostMapping
    @RequestMapping("/update")
    public String update(@Valid @ModelAttribute MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("medicationSummaryPrescriptionInfoCommand", medicationSummaryPrescriptionInfoCommand);
            return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_UPDATE_PAGE;
        }
        this.medicationSummaryPrescriptionService.updatePrescription(medicationSummaryPrescriptionInfoCommand);
        return "redirect:/current-patient/medication-summary/prescription/view-section";
    }
}
