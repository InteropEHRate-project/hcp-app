package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/current-patient/current-diseases")
public class CurrentDiseaseController {
    private CurrentDiseaseService currentDiseaseService;

    public CurrentDiseaseController(CurrentDiseaseService currentDiseaseService) {
        this.currentDiseaseService = currentDiseaseService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("currentDiseaseCommand", currentDiseaseService.currentDiseasesSection());
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("currentDiseaseInfoCommand", new CurrentDiseaseInfoCommand());
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute CurrentDiseaseInfoCommand currentDiseaseInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE;
        }
        currentDiseaseService.insertCurrentDisease(currentDiseaseInfoCommand);
        return "redirect:/current-patient/current-diseases/view-section";
    }
}
