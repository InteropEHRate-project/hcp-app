package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
