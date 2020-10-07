package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.radiologyresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/laboratory-tests/radiology-results")
public class OrganizationController {
    @GetMapping
    @RequestMapping("/organization-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_ORGANIZATION_VIEW;
    }
}
