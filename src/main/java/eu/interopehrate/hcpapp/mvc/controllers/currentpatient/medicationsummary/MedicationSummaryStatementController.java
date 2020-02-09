package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryStatementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
