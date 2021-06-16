package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openEdit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("phExamInfoCommand", this.phExamService.retrieveExamById(id));
        return TemplateNames.CURRENT_PATIENT_PH_EXAM_UPDATE_PAGE;
    }

    @PutMapping
    @RequestMapping("/update")
    public String updateExam(@Valid @ModelAttribute("phExamInfoCommand") PHExamInfoCommand phExamInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_PH_EXAM_UPDATE_PAGE;
        }
        this.phExamService.updateExam(phExamInfoCommand);
        return "redirect:/current-patient/visit-data/ph-exam/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteExam(@RequestParam("id") Long id, Model model) {
        this.phExamService.deleteExam(id);
        model.addAttribute("examDeleted", Boolean.TRUE);
        return this.viewSection(model);
    }

    @GetMapping
    @RequestMapping("/view")
    public String viewExam(@RequestParam("id") Long id, Model model) {
        model.addAttribute("phExamInfoCommand", this.phExamService.retrieveExamById(id));
        return TemplateNames.CURRENT_PATIENT_PH_EXAM_VIEW_EXAM;
    }
}
