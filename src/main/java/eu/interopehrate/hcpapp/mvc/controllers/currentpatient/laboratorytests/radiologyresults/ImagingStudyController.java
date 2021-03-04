package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.radiologyresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/laboratory-tests/radiology-results")
public class ImagingStudyController {

    @GetMapping
    @RequestMapping("/imaging-study-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_RADIOLOGY_RESULTS_IMAGING_STUDY_VIEW;
    }
}
