package ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.commands.HealthCareOrganizationCommand;
import ro.siveco.europeanprojects.iehr.hcpwebapp.services.HealthCareOrganizationService;

@Controller
@RequestMapping("/health-care-organization")
public class HealthCareOrganizationController {
    private HealthCareOrganizationService healthCareOrganizationService;

    public HealthCareOrganizationController(HealthCareOrganizationService healthCareOrganizationService) {
        this.healthCareOrganizationService = healthCareOrganizationService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsTemplate(Model model){
        HealthCareOrganizationCommand healthCareOrganizationCommand = healthCareOrganizationService.getHealthCareOrganization();
        model.addAttribute("healthCareOrganization", healthCareOrganizationCommand);
        return TemplateNames.HEALTH_CARE_ORGANIZATION_VIEW_DETAILS_TEMPLATE;
    }
}
