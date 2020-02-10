package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.ProblemsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/current-patient/problems")
public class ProblemsController {
    private ProblemsService problemsService;

    public ProblemsController(ProblemsService problemsService) {
        this.problemsService = problemsService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("problemCommand", problemsService.problemsSection());
        return TemplateNames.CURRENT_PATIENT_PROBLEMS_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("problemsInfoCommand", new ProblemsInfoCommand());
        return TemplateNames.CURRENT_PATIENT_PROBLEMS_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute ProblemsInfoCommand problemsInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_PROBLEMS_ADD_PAGE;
        }
        problemsService.insertProblem(problemsInfoCommand);
        return "redirect:/current-patient/problems/view-section";
    }
}
