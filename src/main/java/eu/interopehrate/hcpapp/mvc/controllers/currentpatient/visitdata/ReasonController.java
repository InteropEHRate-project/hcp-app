package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.ReasonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/visit-data/reason")
public class ReasonController {
    private final ReasonService reasonService;

    public ReasonController(ReasonService reasonService) {
        this.reasonService = reasonService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_REASON_VIEW_SECTION;
    }

    @PostMapping
    @RequestMapping("/save-note")
    public String saveAdd(String note) {
        this.reasonService.insertNote(note);
        return "redirect:/current-patient/visit-data/reason/view-section";
    }
}
