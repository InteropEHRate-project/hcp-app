package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/pat-history")
public class PatHistoryController {
    private final PatHistoryService patHistoryService;

    public PatHistoryController(PatHistoryService patHistoryService) {
        this.patHistoryService = patHistoryService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patHistoryCommand", this.patHistoryService.patHistorySection());
        model.addAttribute("patHis", this.patHistoryService.patHistorySection().getListOfPatHis());
        model.addAttribute("socHis", this.patHistoryService.patHistorySection().getListOfSocHis());
        model.addAttribute("famHis", this.patHistoryService.patHistorySection().getListOfFamHis());
        return TemplateNames.CURRENT_PATIENT_PAT_HISTORY_VIEW_SECTION;
    }

    @PostMapping
    @RequestMapping("/save")
    public String saveSocHis(String patHis, String socHis, String famHis) {
        this.patHistoryService.insertPatHis(patHis);
        this.patHistoryService.insertSocHis(socHis);
        this.patHistoryService.insertFamHis(famHis);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @GetMapping
    @RequestMapping("/editRisk")
    public String editRisk(Model model,
                           @RequestParam(name = "value") Boolean value,
                           @RequestParam(name = "id") String id) {
        this.patHistoryService.editRisk(value, id);
        model.addAttribute("riskFactorEdited", Boolean.TRUE);
        return this.viewSection(model);
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openEditDiagnosis(@RequestParam("id") String id, Model model) {
        model.addAttribute("patHisInfoCommand", this.patHistoryService.patHisInfoCommandById(id));
        return TemplateNames.CURRENT_PATIENT_PAT_HISTORY_UPDATE_PAGE;
    }

    @PutMapping
    @RequestMapping("/update")
    public String updateDiagnosis(@Valid @ModelAttribute("patHisInfoCommand") PatHistoryInfoCommandDiagnosis patHisInfoCommand, BindingResult bindingResult) {
        if (Objects.nonNull(patHisInfoCommand.getYearOfDiagnosis()) && (patHisInfoCommand.getYearOfDiagnosis() > 9999 || patHisInfoCommand.getYearOfDiagnosis() < 0)) {
            bindingResult.addError(new FieldError("patHisInfoCommand", "yearOfDiagnosis", "exceeds range 0 - 9999"));
        }
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_PAT_HISTORY_UPDATE_PAGE;
        }
        this.patHistoryService.updateDiagnosis(patHisInfoCommand);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteDiagnosis(@RequestParam("id") String id, Model model) {
        this.patHistoryService.deleteDiagnosis(id);
        model.addAttribute("diagnosisDeleted", Boolean.TRUE);
        return this.viewSection(model);
    }

    @GetMapping
    @RequestMapping("/refresh")
    public String refresh() throws Exception {
        this.patHistoryService.refresh();
        return "redirect:/current-patient/pat-history/view-section";
    }
}
