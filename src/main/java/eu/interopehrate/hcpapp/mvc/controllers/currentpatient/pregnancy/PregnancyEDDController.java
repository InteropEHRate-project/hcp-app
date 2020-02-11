package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.pregnancy;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/pregnancy/edd")
public class PregnancyEDDController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_PREGNANCY_VIEW_SECTION_EDD;
    }
}
