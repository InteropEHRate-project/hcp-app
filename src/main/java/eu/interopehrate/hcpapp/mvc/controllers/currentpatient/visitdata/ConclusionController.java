package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.ConclusionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/visit-data/conclusion")
public class ConclusionController {
    private final ConclusionService conclusionService;

    public ConclusionController(ConclusionService conclusionService) {
        this.conclusionService = conclusionService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("conclusionCommand", this.conclusionService.conclusionComm());
        return TemplateNames.CURRENT_PATIENT_CONCLUSION_VIEW_SECTION;
    }
}
