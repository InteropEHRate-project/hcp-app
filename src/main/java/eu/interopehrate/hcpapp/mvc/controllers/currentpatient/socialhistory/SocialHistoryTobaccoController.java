package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.socialhistory;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/social-history/tobacco")
public class SocialHistoryTobaccoController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_SOCIAL_HISTORY_TOBACCO_VIEW_SECTION;
    }
}
