package ro.siveco.europeanprojects.iehr.hcpwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/health-care-organization")
public class HealthCareOrganizationController {

    @RequestMapping("/view-details")
    public String detailsTemplate(){
        return TemplateNames.HEALTH_CARE_ORGANIZATION_DETAILS_TEMPLATE;
    }
}
