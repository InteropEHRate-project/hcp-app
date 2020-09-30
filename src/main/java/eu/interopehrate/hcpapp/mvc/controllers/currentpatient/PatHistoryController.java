package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/current-patient/pat-history")
public class PatHistoryController {
    private PatHistoryService patHistoryService;

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
    @RequestMapping("/save-patHis")
    public String savePatHis(String patHis) {
        this.patHistoryService.insertPatHis(patHis);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-patHis")
    public String deletePatHis(@RequestParam("patHis") String patHis) {
        this.patHistoryService.deletePatHis(patHis);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @PostMapping
    @RequestMapping("/save-socHis")
    public String saveSocHis(String socHis) {
        this.patHistoryService.insertSocHis(socHis);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-socHis")
    public String deleteSocHis(@RequestParam("socHis") String socHis) {
        this.patHistoryService.deleteSocHis(socHis);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @PostMapping
    @RequestMapping("/save-famHis")
    public String saveFamHis(String famHis) {
        this.patHistoryService.insertFamHis(famHis);
        return "redirect:/current-patient/pat-history/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-famHis")
    public String deleteFamHis(@RequestParam("famHis") String famHis) {
        this.patHistoryService.deleteFamHis(famHis);
        return "redirect:/current-patient/pat-history/view-section";
    }
}
