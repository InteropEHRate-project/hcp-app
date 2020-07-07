package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.CurrentMedicationsStatementCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.CurrentMedicationsStatementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/current-patient/current-medications/statement")
public class CurrentMedicationsStatementController {
    private final CurrentMedicationsStatementService currentMedicationsStatementService;

    public CurrentMedicationsStatementController(CurrentMedicationsStatementService currentMedicationsStatementService) {
        this.currentMedicationsStatementService = currentMedicationsStatementService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("statementCommand", currentMedicationsStatementService.statementCommand());
        return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("currentMedicationsStatementCommand", new CurrentMedicationsStatementCommand());
        return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute CurrentMedicationsStatementCommand currentMedicationsStatementCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_ADD_PAGE;
        }
        currentMedicationsStatementService.insertCurrentMedicationsStatement(currentMedicationsStatementCommand);
        return "redirect:/current-patient/current-medications/statement/view-section";
    }
}
