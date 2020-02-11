package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.laboratoryresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-results/laboratory-results")
public class SpecimenController {
    @GetMapping
    @RequestMapping("/specimen-view")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_SPECIMEN_VIEW;
    }
}
