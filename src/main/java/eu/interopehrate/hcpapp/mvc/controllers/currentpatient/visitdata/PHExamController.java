package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
        model.addAttribute("patient", this.phExamService.getCurrentPatient().getPatient());
        model.addAttribute("list", this.phExamService.getListOfExams());
        return TemplateNames.CURRENT_PATIENT_PH_EXAM_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("phExamInfoCommand", new PHExamInfoCommand());
        return TemplateNames.CURRENT_PATIENT_PH_EXAM_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute PHExamInfoCommand phExamInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_PH_EXAM_ADD_PAGE;
        }
        this.phExamService.insertExam(phExamInfoCommand);
        return "redirect:/current-patient/visit-data/ph-exam/view-section";
    }
}
