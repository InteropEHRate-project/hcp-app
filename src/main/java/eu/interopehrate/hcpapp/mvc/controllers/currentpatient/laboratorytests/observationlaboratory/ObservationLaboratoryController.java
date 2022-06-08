package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.observationlaboratory;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/laboratory-tests/laboratory-results")
public class ObservationLaboratoryController {
    private final ObservationLaboratoryService observationLaboratoryService;
    public static ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis;

    public ObservationLaboratoryController(ObservationLaboratoryService observationLaboratoryService) {
        this.observationLaboratoryService = observationLaboratoryService;
    }

    @GetMapping
    @RequestMapping("/observation-laboratory-view")
    public String viewSection(Model model, HttpSession session, String keyword) {
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("emergency", CurrentPatient.typeOfWorkingSession.toString());
        }
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("MVisit", CurrentPatient.typeOfWorkingSession.toString());
        }
        session.setAttribute("keyword", keyword);
        model.addAttribute("patient", this.observationLaboratoryService.getCurrentPatient().getPatient());
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

    @GetMapping
    @RequestMapping("/refresh-data")
    public String refreshData() {
        this.observationLaboratoryService.refreshData();
        return "redirect:/current-patient/laboratory-tests/laboratory-results/observation-laboratory-view";
    }

    @GetMapping
    @RequestMapping("/refresh")
    public String refresh() throws Exception {
        this.observationLaboratoryService.getLaboratoryTests();
        return "redirect:/current-patient/laboratory-tests/laboratory-results/observation-laboratory-view";
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
       // model.addAttribute("prescriptionTypes", this.observationLaboratoryService.getPrescriptionTypesRepository().findAll());
        model.addAttribute("laboratoryInfoCommand", new ObservationLaboratoryInfoCommandAnalysis());
        return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
           // model.addAttribute("prescriptionTypes", this.observationLaboratoryService.getPrescriptionTypesRepository().findAll());
            return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_ADD_PAGE;
        }
        observationLaboratoryService.insertLaboratory(observationLaboratoryInfoCommandAnalysis);
        return "redirect:/current-patient/laboratory-tests/laboratory-results/observation-laboratory-view";
    }
}
