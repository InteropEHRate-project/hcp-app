package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyIntoleranceService;
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
@RequestMapping("/current-patient/allergies-intolerances")
public class AllergyIntoleranceController {
    private AllergyIntoleranceService allergyIntoleranceService;

    public AllergyIntoleranceController(AllergyIntoleranceService allergyIntoleranceService) {
        this.allergyIntoleranceService = allergyIntoleranceService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfo = allergyIntoleranceService.allergyIntoleranceSection();
        model.addAttribute("allergyIntolerance", new AllergyIntoleranceCommand(allergyIntoleranceInfo));
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_INTOLERANCES_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("allergyIntoleranceInfoCommand", new AllergyIntoleranceInfoCommand());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_INTOLERANCES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_ALLERGIES_INTOLERANCES_ADD_PAGE;
        }
        allergyIntoleranceService.insertAllergyIntolerance(allergyIntoleranceInfoCommand);
        return "redirect:/current-patient/allergies-intolerances/view-section";
    }
}
