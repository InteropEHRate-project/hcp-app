package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceInfoCommand;
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

@Controller
@RequestMapping("/current-patient/allergies")
public class AllergyIntoleranceController {
    private final AllergyIntoleranceService allergyIntoleranceService;

    public AllergyIntoleranceController(AllergyIntoleranceService allergyIntoleranceService) {
        this.allergyIntoleranceService = allergyIntoleranceService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patient", this.allergyIntoleranceService.getCurrentPatient().getPatient());
        model.addAttribute("allergyIntolerance", this.allergyIntoleranceService.allergyIntoleranceInfoCommand());
        model.addAttribute("allergyIntoleranceTranslated", this.allergyIntoleranceService.allergyIntoleranceInfoCommandTranslated());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("allergyIntoleranceInfoCommand", new AllergyIntoleranceInfoCommand());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_ALLERGIES_ADD_PAGE;
        }
        this.allergyIntoleranceService.insertAllergyIntolerance(allergyIntoleranceInfoCommand);
        return "redirect:/current-patient/allergies/view-section";
    }
}
