package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.laboratoryresults;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/current-patient/diagnostic-results/laboratory-results")
public class ResultController {
    private ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    @RequestMapping("/idToResult")
    public String viewSection(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("result", resultService.resultInfoCommand(id));
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_RESULT_VIEW;
    }
}
