package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyService;
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
public class AllergyController {
    private final AllergyService allergyService;

    public AllergyController(AllergyService allergyService) {
        this.allergyService = allergyService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patient", this.allergyService.getCurrentPatient().getPatient());
        model.addAttribute("allergyIntolerance", this.allergyService.allergyInfoCommand());
        model.addAttribute("allergyIntoleranceTranslated", this.allergyService.allergyInfoCommandTranslated());
        model.addAttribute("newAllergies", this.allergyService.listOfNewAllergies());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("allergyInfoCommand", new AllergyInfoCommand());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute AllergyInfoCommand allergyInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_ALLERGIES_ADD_PAGE;
        }
        this.allergyService.insertAllergy(allergyInfoCommand);
        return "redirect:/current-patient/allergies/view-section";
    }
}
