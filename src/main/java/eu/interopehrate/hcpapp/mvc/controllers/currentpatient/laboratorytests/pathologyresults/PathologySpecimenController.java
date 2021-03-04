package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.pathologyresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/laboratory-tests/pathology-results")
public class PathologySpecimenController {

    @GetMapping
    @RequestMapping("/specimen-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_PATHOLOGY_RESULTS_SPECIMEN_VIEW;
    }
}
