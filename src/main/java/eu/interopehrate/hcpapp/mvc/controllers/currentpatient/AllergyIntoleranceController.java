package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyIntoleranceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfo = allergyIntoleranceService.allergiesIntolerancesSection();
        model.addAttribute("allergyIntolerance", new AllergyIntoleranceCommand(allergyIntoleranceInfo));
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_INTOLERANCES_VIEW_SECTION;
    }
}
