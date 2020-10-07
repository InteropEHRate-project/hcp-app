package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.pathologyresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/laboratory-tests/pathology-results")
public class PathologyOrganisationLaboratoryController {
    @GetMapping
    @RequestMapping("/organization-laboratory-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_LABORATORY_RESULTS_PATHOLOGY_RESULTS_ORGANISATION_LABORATORY_VIEW;
    }
}
