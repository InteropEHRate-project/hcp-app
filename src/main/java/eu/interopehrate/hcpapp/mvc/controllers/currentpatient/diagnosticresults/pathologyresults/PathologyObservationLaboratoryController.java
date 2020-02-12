package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.pathologyresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-results/pathology-results")
public class PathologyObservationLaboratoryController {

    @GetMapping
    @RequestMapping("/observation-laboratory-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_PATHOLOGY_RESULTS_OBSERVATION_PATHOLOGY_VIEW;
    }
}
