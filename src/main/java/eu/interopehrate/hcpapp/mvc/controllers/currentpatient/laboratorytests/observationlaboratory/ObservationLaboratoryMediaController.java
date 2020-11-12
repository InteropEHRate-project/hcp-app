package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/laboratory-tests/laboratory-results")
public class ObservationLaboratoryMediaController {
//    private final ObservationLaboratoryMediaService observationLaboratoryMediaService;
//
//    public ObservationLaboratoryMediaController(ObservationLaboratoryMediaService observationLaboratoryMediaService) {
//        this.observationLaboratoryMediaService = observationLaboratoryMediaService;
//    }

    @GetMapping
    @RequestMapping("/observation-laboratory-media-view")
    public String viewSection(Model model) {
//        model.addAttribute("imageCommand", observationLaboratoryMediaService.imageCommand());
        return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW;
    }
}
