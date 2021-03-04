package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/current-patient/laboratory-tests/laboratory-results")
public class ObservationLaboratoryController {
    private final ObservationLaboratoryService observationLaboratoryService;

    public ObservationLaboratoryController(ObservationLaboratoryService observationLaboratoryService) {
        this.observationLaboratoryService = observationLaboratoryService;
    }

    @GetMapping
    @RequestMapping("/observation-laboratory-view")
    public String viewSection(Model model, HttpSession session, String keyword) {
        session.setAttribute("keyword", keyword);
        return this.findPaginated(1, model, keyword);
    }

    @GetMapping
    @RequestMapping("/observation-laboratory-view/page/{pageNo}/keyword/{keyword}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model,
                                @PathVariable(value = "keyword") String keyword) {
        this.observationLaboratoryService.observationLaboratoryInfoCommandAnalysis(keyword).createPagination(pageNo);
        model.addAttribute("labResultsAnalysisCommand", this.observationLaboratoryService.observationLaboratoryInfoCommandAnalysis(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", ObservationLaboratoryCommandAnalysis.getTotalPages());
        model.addAttribute("totalItems", ObservationLaboratoryCommandAnalysis.getTotalElements());
        model.addAttribute("isFiltered", this.observationLaboratoryService.isFiltered());
        model.addAttribute("isEmpty", this.observationLaboratoryService.isEmpty());
        return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_VIEW;
    }
}
