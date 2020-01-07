package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticResultService;
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
@RequestMapping("/current-patient/diagnostic-result")
public class DiagnosticResultController {
    private DiagnosticResultService diagnosticResultService;

    public DiagnosticResultController(DiagnosticResultService diagnosticResultService) {
        this.diagnosticResultService = diagnosticResultService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        List<DiagnosticResultInfoCommand> diagnosticCommandInfo = diagnosticResultService.diagnosticResultSection();
        model.addAttribute("diagnosticResult", new DiagnosticResultCommand(diagnosticCommandInfo));
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("diagnosticResultInfoCommand", new DiagnosticResultInfoCommand());
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute DiagnosticResultInfoCommand diagnosticResultInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_ADD_PAGE;
        }
        diagnosticResultService.insertDiagnosticResult(diagnosticResultInfoCommand);
        return "redirect:/current-patient/diagnostic-result/view-section";
    }
}
