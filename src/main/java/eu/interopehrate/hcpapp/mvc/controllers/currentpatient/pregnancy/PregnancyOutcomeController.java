package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.pregnancy;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/pregnancy/outcome")
public class PregnancyOutcomeController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_PREGNANCY_OUTCOME_VIEW_SECTION;
    }
}
