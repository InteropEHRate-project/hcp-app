package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.ReasonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/current-patient/visit-data/reason")
public class ReasonController {
    private final ReasonService reasonService;

    public ReasonController(ReasonService reasonService) {
        this.reasonService = reasonService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String detailsTemplate(Model model) {
        model.addAttribute("reasons", this.reasonService.getReasons());
        model.addAttribute("reason", new ReasonEntity());
        return TemplateNames.CURRENT_PATIENT_REASON_VIEW_SECTION;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute("symptom") String symptom) {
        this.reasonService.addSymptom(symptom);
        return "redirect:/current-patient/visit-data/reason/view-section";
    }

    @GetMapping
    @RequestMapping("/delete")
    public String delete(@RequestParam("symptomId") Long symptomId) {
        this.reasonService.delete(symptomId);
        return "redirect:/current-patient/visit-data/reason/view-section";
    }
}
