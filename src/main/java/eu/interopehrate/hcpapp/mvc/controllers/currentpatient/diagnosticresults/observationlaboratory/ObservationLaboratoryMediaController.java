package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryMediaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-results/laboratory-results")
public class ObservationLaboratoryMediaController {
    private final ObservationLaboratoryMediaService observationLaboratoryMediaService;

    public ObservationLaboratoryMediaController(ObservationLaboratoryMediaService observationLaboratoryMediaService) {
        this.observationLaboratoryMediaService = observationLaboratoryMediaService;
    }

    @GetMapping
    @RequestMapping("/observation-laboratory-media-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW;
    }

    @GetMapping
    @RequestMapping("/view-ecg")
    public String viewEcg() {
        observationLaboratoryMediaService.displayEcgDemo();
        return "redirect:/current-patient/diagnostic-results/laboratory-results/observation-laboratory-media-view";
    }

    @GetMapping
    @RequestMapping("/view-mr")
    public String viewMr() {
        observationLaboratoryMediaService.displayMrDemo();
        return "redirect:/current-patient/diagnostic-results/laboratory-results/observation-laboratory-media-view";
    }
}
