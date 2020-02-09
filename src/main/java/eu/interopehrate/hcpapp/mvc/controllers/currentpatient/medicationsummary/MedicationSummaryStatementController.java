package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryStatementCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryStatementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/current-patient/medication-summary/statement")
public class MedicationSummaryStatementController {
    private final MedicationSummaryStatementService medicationSummaryStatementService;

    public MedicationSummaryStatementController(MedicationSummaryStatementService medicationSummaryStatementService) {
        this.medicationSummaryStatementService = medicationSummaryStatementService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("statementCommand", medicationSummaryStatementService.statementCommand());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_STATEMENT_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("medicationSummaryStatementCommand", new MedicationSummaryStatementCommand());
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_STATEMENT_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute MedicationSummaryStatementCommand medicationSummaryStatementCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_STATEMENT_ADD_PAGE;
        }
        medicationSummaryStatementService.insertMedicationSummaryStatement(medicationSummaryStatementCommand);
        return "redirect:/current-patient/medication-summary/statement/view-section";
    }
}
