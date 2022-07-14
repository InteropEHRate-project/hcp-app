package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.laboratorytests.observationlaboratory;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsTypesEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsTypesRepository;
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
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/laboratory-tests/laboratory-results")
public class ObservationLaboratoryController {

    public static final String EN = "en";

    private final ObservationLaboratoryService observationLaboratoryService;
    private final LaboratoryTestsTypesRepository laboratoryTestsTypesRepository;

    public ObservationLaboratoryController(ObservationLaboratoryService observationLaboratoryService, LaboratoryTestsTypesRepository laboratoryTestsTypesRepository) {
        this.observationLaboratoryService = observationLaboratoryService;
        this.laboratoryTestsTypesRepository = laboratoryTestsTypesRepository;
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
        model.addAttribute("laboratoryUpload", observationLaboratoryService.laboratoryUpload());
        model.addAttribute("observationLaboratoryService", observationLaboratoryService.getCurrentD2DConnection());
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
        model.addAttribute("laboratory", this.observationLaboratoryService.observationCommand());
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
    public String openAddPage(Model model, @RequestParam(required = false) String lang) {
        model.addAttribute("observationLaboratoryInfoCommandAnalysis", new ObservationLaboratoryInfoCommandAnalysis());
        List<LaboratoryTestsTypesEntity> listOfLaboratoryTestsTypes = null != lang && (lang.equals("en") || lang.equals("fr")
                || lang.equals("it") || lang.equals("el") || lang.equals("ro")) ?
                this.laboratoryTestsTypesRepository.findAllByLang(lang) :
                this.laboratoryTestsTypesRepository.findAllByLang(EN);

        model.addAttribute("laboratoryTypes",listOfLaboratoryTestsTypes);
        model.addAttribute("correlations", this.observationLaboratoryService.correlations());
        return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("laboratoryTypes", this.laboratoryTestsTypesRepository.findAll());
            model.addAttribute("correlations", this.observationLaboratoryService.correlations());
            return TemplateNames.CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_ADD_PAGE;
        }
        observationLaboratoryService.insertLaboratory(observationLaboratoryInfoCommandAnalysis);
        return "redirect:/current-patient/laboratory-tests/laboratory-results/observation-laboratory-view";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteData(@RequestParam(name = "an") String an, @RequestParam(name = "sample") String sample) throws IOException {
        this.observationLaboratoryService.deleteLaboratory(an, sample);
        return "redirect:/current-patient/laboratory-tests/laboratory-results/observation-laboratory-view";
    }

}
