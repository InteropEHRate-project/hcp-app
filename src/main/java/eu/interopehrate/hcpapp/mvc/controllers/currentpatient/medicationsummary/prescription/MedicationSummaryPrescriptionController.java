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
import java.time.LocalDate;

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
    public String delete(@RequestParam("drugId") String drugId) {
        this.medicationSummaryPrescriptionService.deletePrescription(drugId);
        return "redirect:/current-patient/medication-summary/prescription/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openUpdatePage(@RequestParam("id") String id, Model model) {
        MedicationSummaryPrescriptionInfoCommand drug = null;
        for (int i = 0; i < this.medicationSummaryPrescriptionService.getMedicationSummaryPrescriptionInfoCommandList().size(); i++) {
            if (this.medicationSummaryPrescriptionService.getMedicationSummaryPrescriptionInfoCommandList().get(i).getId().equalsIgnoreCase(id)) {
                drug = this.medicationSummaryPrescriptionService.getMedicationSummaryPrescriptionInfoCommandList().get(i);
                break;
            }
        }
        model.addAttribute("drug", drug);
        return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_UPDATE_PAGE;
    }

    @PutMapping
    @RequestMapping("/update")
    public String update(@RequestParam("id") String id,
                         @RequestParam("status") String status,
                         @RequestParam("timings") String timings,
                         @RequestParam("drugName") String drugName,
                         @RequestParam("drugDosage") String drugDosage,
                         @RequestParam("notes") String notes,
                         @RequestParam("dateOfPrescription") LocalDate dateOfPrescription) {
        this.medicationSummaryPrescriptionService.updatePrescription(id, status, timings, drugName, drugDosage, notes, dateOfPrescription);
        return "redirect:/current-patient/medication-summary/prescription/view-section";
    }
}
