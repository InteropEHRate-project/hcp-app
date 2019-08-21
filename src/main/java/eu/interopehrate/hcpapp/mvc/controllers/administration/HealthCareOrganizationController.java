package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.commands.HealthCareOrganizationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/health-care-organization")
public class HealthCareOrganizationController {
    private HealthCareOrganizationService healthCareOrganizationService;

    public HealthCareOrganizationController(HealthCareOrganizationService healthCareOrganizationService) {
        this.healthCareOrganizationService = healthCareOrganizationService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsTemplate(Model model) {
        HealthCareOrganizationCommand healthCareOrganizationCommand = healthCareOrganizationService.getHealthCareOrganization();
        model.addAttribute("healthCareOrganization", healthCareOrganizationCommand);
        return TemplateNames.ADMINISTRATION_HEALTH_CARE_ORGANIZATION_VIEW_DETAILS;
    }
}
