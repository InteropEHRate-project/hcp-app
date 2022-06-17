package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("phExamCommand", this.phExamService.phExamCommand());
        model.addAttribute("phExam", this.phExamService.phExamCommand().getListClinicalExam());
        model.addAttribute("infoPhExam", new PHExamInfoCommand());
        model.addAttribute("newPhExam", this.phExamService.getNewPhExam());
        return TemplateNames.CURRENT_PATIENT_PH_EXAM_VIEW_SECTION;
    }

    @PostMapping
    @RequestMapping("/save-clinical-exam")
    public String saveClinicalExam(String phExam, @ModelAttribute PHExamInfoCommand phExamInfoCommand) {
        this.phExamService.insertClinicalExam(phExam);
        this.phExamService.insertPhExam(phExamInfoCommand);
        return "redirect:/current-patient/visit-data/ph-exam/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteAll(String note) {
        this.phExamService.delete(note);
        return "redirect:/current-patient/visit-data/ph-exam/view-section";
    }
}
