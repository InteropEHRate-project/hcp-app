package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/visit-data/ph-exam")
public class PHExamController {
    private final PHExamService phExamService;

    public PHExamController(PHExamService phExamService) {
        this.phExamService = phExamService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("list", this.phExamService.getListOfExams());
        return TemplateNames.CURRENT_PATIENT_PH_EXAM_VIEW_SECTION;
    }
}
