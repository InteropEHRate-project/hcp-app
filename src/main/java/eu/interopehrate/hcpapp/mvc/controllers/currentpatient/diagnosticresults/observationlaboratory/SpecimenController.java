package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.SpecimenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/current-patient/diagnostic-results/laboratory-results")
public class SpecimenController {
    private SpecimenService specimenService;

    public SpecimenController(SpecimenService specimenService) {
        this.specimenService = specimenService;
    }

    @GetMapping
    @RequestMapping("/idToSpecimen")
    public String viewSection(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("specimen", specimenService.specimenInfoCommand(id));
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_SPECIMEN_VIEW;
    }
}
