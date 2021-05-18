package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/current-patient/current-diseases")
public class CurrentDiseaseController {
    private final CurrentDiseaseService currentDiseaseService;
    public static List<CurrentDiseaseEntity> currentDiseaseEntityList;

    public CurrentDiseaseController(CurrentDiseaseService currentDiseaseService) {
        this.currentDiseaseService = currentDiseaseService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
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
        model.addAttribute("currentDiseasesList", this.currentDiseaseService.listNewCurrentDiseases());
        return TemplateNames.CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute CurrentDiseaseInfoCommand currentDiseaseInfoCommand, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentDiseasesList", this.currentDiseaseService.listNewCurrentDiseases());
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

    @DeleteMapping
    @RequestMapping("/delete")
    public String delete(@RequestParam("note") String note) {
        this.currentDiseaseService.deleteNote(note);
        return "redirect:/current-patient/current-diseases/view-section";
    }
}
