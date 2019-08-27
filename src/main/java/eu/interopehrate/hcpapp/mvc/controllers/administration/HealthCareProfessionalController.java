package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/health-care-professional")
public class HealthCareProfessionalController {
    private HealthCareProfessionalService healthCareProfessionalService;

    public HealthCareProfessionalController(HealthCareProfessionalService healthCareProfessionalService) {
        this.healthCareProfessionalService = healthCareProfessionalService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String viewDetails(Model model) {
        model.addAttribute("healthCareProfessional", healthCareProfessionalService.getHealthCareProfessional());
        return TemplateNames.ADMINISTRATION_HEALTH_CARE_PROFESSIONAL_VIEW_DETAILS;
    }
}
