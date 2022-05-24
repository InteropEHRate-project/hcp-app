package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/current-diseases")
public class CurrentDiseaseController {
    private final CurrentDiseaseService currentDiseaseService;

    public CurrentDiseaseController(CurrentDiseaseService currentDiseaseService) {
        this.currentDiseaseService = currentDiseaseService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model, HttpSession session) {
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("emergency", CurrentPatient.typeOfWorkingSession.toString());
        }
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("medicalVisit", CurrentPatient.typeOfWorkingSession.toString());
        }
        model.addAttribute("patient", this.currentDiseaseService.getCurrentPatient().getPatient());
        model.addAttribute("currentDiseaseCommand", this.currentDiseaseService.currentDiseasesSection());
        model.addAttribute("currentDiseasesList", this.currentDiseaseService.listNewCurrentDiseases());
        model.addAttribute("notes", this.currentDiseaseService.currentDiseasesSection().getListOfNotes());
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("currentDiseaseInfoCommand", new CurrentDiseaseInfoCommand());
        model.addAttribute("currentDiseaseTypes", this.currentDiseaseService.getCurrentTypesRepository().findAll());
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute CurrentDiseaseInfoCommand currentDiseaseInfoCommand, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentDiseaseTypes", this.currentDiseaseService.getCurrentTypesRepository().findAll());
            return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE;
        }
        this.currentDiseaseService.insertCurrentDisease(currentDiseaseInfoCommand);
        return "redirect:/current-patient/current-diseases/view-section";
    }

    @PostMapping
    @RequestMapping("/save-note")
    public String saveAdd(String note) {
        this.currentDiseaseService.insertNote(note);
        return "redirect:/current-patient/current-diseases/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openEditCurrentDisease(@RequestParam("id") String id, Model model) {
        model.addAttribute("currentDiseaseInfoCommandUpdate", this.currentDiseaseService.currentDiseaseById(id));
        model.addAttribute("currentDiseaseTypes", this.currentDiseaseService.getCurrentTypesRepository().findAll());
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASE_UPDATE_PAGE;
    }

    @GetMapping
    @RequestMapping("/open-update-page-data-base")
    public String openEditCurrentDiseaseDataBase(@RequestParam("id") Long id, Model model) {
        model.addAttribute("currentDiseaseInfoCommandUpdateDataBase", this.currentDiseaseService.retrieveNewCurrentDiseaseById(id));
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASE_UPDATE_PAGE_DB;
    }

    @PutMapping
    @RequestMapping("/update")
    public String updateCurrentDisease(@Valid @ModelAttribute("currentDiseaseInfoCommandUpdate") CurrentDiseaseInfoCommand currentDiseaseInfoCommandUpdate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASE_UPDATE_PAGE;
        }
        this.currentDiseaseService.updateCurrentDisease(currentDiseaseInfoCommandUpdate);
        return "redirect:/current-patient/current-diseases/view-section";
    }

    @PutMapping
    @RequestMapping("/update-data-base")
    public String updateCurrentDiseaseDataBase(@Valid @ModelAttribute("currentDiseaseInfoCommandUpdateDataBase") CurrentDiseaseInfoCommand currentDiseaseInfoCommandUpdate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASE_UPDATE_PAGE_DB;
        }
        this.currentDiseaseService.updateNewCurrentDisease(currentDiseaseInfoCommandUpdate);
        return "redirect:/current-patient/current-diseases/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String delete(@RequestParam("note") String note) {
        this.currentDiseaseService.deleteNote(note);
        return "redirect:/current-patient/current-diseases/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-current-disease")
    public String deleteCurrentDiseaseFromSEHR(@RequestParam("id") String id, Model model, HttpSession session) {
        this.currentDiseaseService.deleteCurrentDisease(id);
        model.addAttribute("currentDiseaseDeletedFromSEHR", Boolean.TRUE);
        return this.viewSection(model, session);
    }

    @DeleteMapping
    @RequestMapping("/delete-current-disease-from-db")
    public String deleteCurrentDisease(@RequestParam("id") Long id, Model model, HttpSession session) {
        this.currentDiseaseService.deleteNewCurrentDisease(id);
        model.addAttribute("currentDiseaseDeletedFromDataBase", Boolean.TRUE);
        return this.viewSection(model, session);
    }

    @GetMapping
    @RequestMapping("/refresh")
    public String refresh() throws Exception {
        this.currentDiseaseService.refresh();
        return "redirect:/current-patient/current-diseases/view-section";
    }

    @GetMapping
    @RequestMapping("/refresh-data")
    public String refreshData() {
        this.currentDiseaseService.refreshData();
        return "redirect:/current-patient/current-diseases/view-section";
    }
}
