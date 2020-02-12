package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.radiologyresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-results/radiology-results")
public class PractitionerController {
    @GetMapping
    @RequestMapping("/practitioner-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_PRACTITIONER_VIEW;
    }
}
