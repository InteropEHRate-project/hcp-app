package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/health-care-professional")
public class HealthCareProfessionalController {
    @GetMapping
    @RequestMapping("/view-details")
    public String viewDetails() {
        return TemplateNames.ADMINISTRATION_HEALTH_CARE_PROFESSIONAL_VIEW_DETAILS;
    }
}
