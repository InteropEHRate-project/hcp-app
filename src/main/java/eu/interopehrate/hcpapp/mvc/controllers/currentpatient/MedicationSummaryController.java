package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.MedicationSummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/current-patient/medication-summary")
public class MedicationSummaryController {
    private MedicationSummaryService medicationSummaryService;

    public MedicationSummaryController(MedicationSummaryService medicationSummaryService) {
        this.medicationSummaryService = medicationSummaryService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        List<MedicationSummaryInfoCommand> medicationSummaryInfo = medicationSummaryService.medicationSummarySection();
        model.addAttribute("medicationSummary", new MedicationSummaryCommand(medicationSummaryInfo));
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("medicationSummaryInfoCommand", new MedicationSummaryInfoCommand());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute MedicationSummaryInfoCommand medicationSummaryInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_ADD_PAGE;
        }
        medicationSummaryService.insertMedicationSummary(medicationSummaryInfoCommand);
        return "redirect:/current-patient/medication-summary/view-section";
    }
}
