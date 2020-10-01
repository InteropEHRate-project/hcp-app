package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/current-patient/diagnostic-results/laboratory-results")
public class ObservationLaboratoryController {
    private ObservationLaboratoryService observationLaboratoryService;

    public ObservationLaboratoryController(ObservationLaboratoryService observationLaboratoryService) {
        this.observationLaboratoryService = observationLaboratoryService;
    }

    @GetMapping
    @RequestMapping("/observation-laboratory-view")
    public String viewSection(Model model) {
        return this.findPaginated(1, model);
    }

    @GetMapping
    @RequestMapping("/observation-laboratory-view/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        //PAGE SIZE is hardcoded HERE
        int pageSize = 10;
        ObservationLaboratoryCommandAnalysis labResultsAnalysisCommand = this.observationLaboratoryService.observationLaboratoryInfoCommandAnalysis(pageNo, pageSize);
        List<ObservationLaboratoryInfoCommandAnalysis> infoCommandAnalysisList = labResultsAnalysisCommand.getObservationLaboratoryInfoCommandAnalyses().getContent();
        model.addAttribute("labResultsAnalysisCommand", labResultsAnalysisCommand);
        model.addAttribute("infoCommandAnalysisList", infoCommandAnalysisList);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", labResultsAnalysisCommand.getObservationLaboratoryInfoCommandAnalyses().getTotalPages());
        model.addAttribute("totalItems", labResultsAnalysisCommand.getObservationLaboratoryInfoCommandAnalyses().getTotalElements());
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_OBSERVATION_LABORATORY_VIEW;
    }
}
