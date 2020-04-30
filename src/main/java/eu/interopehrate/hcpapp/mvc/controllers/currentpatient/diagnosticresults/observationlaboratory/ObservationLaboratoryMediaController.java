package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-results/laboratory-results")
public class ObservationLaboratoryMediaController {
    @GetMapping
    @RequestMapping("/observation-laboratory-media-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW;
    }
}
