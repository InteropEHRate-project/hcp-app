package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergiesIntolerancesCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergiesIntolerancesInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.AllergiesIntolerancesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/current-patient/allergies-intolerances")
public class AllergiesIntolerancesController {
    private AllergiesIntolerancesService allergiesIntolerancesService;

    public AllergiesIntolerancesController(AllergiesIntolerancesService allergiesIntolerancesService) {
        this.allergiesIntolerancesService = allergiesIntolerancesService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        List<AllergiesIntolerancesInfoCommand> allergiesIntolernacesInfo = allergiesIntolerancesService.allergiesIntolerancesSection();
        model.addAttribute("allergiesIntolerances", new AllergiesIntolerancesCommand(allergiesIntolernacesInfo));
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_INTOLERANCES_VIEW_SECTION;
    }
}
