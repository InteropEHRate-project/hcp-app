package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.InstrumentsExaminationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/current-patient/visit-data/instr-exam")
public class InstrumentsExaminationController {
    private final InstrumentsExaminationService instrumentsExaminationService;

    public InstrumentsExaminationController(InstrumentsExaminationService instrumentsExaminationService) {
        this.instrumentsExaminationService = instrumentsExaminationService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patient", this.instrumentsExaminationService.getCurrentPatient().getPatient());
        model.addAttribute("instrExamCommand", this.instrumentsExaminationService.instrExam());
        return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_VIEW_PAGE;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("instrExamInfoCommand", new InstrumentsExaminationInfoCommand());
        return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_ADD_PAGE;
        }
        this.instrumentsExaminationService.insertInstrExam(instrumentsExaminationInfoCommand);
        return "redirect:/current-patient/visit-data/instr-exam/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openEdit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("instrExamInfoCommand", this.instrumentsExaminationService.retrieveInstrExamById(id));
        return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_UPDATE_PAGE;
    }

    @PutMapping
    @RequestMapping("/update")
    public String updateExam(@Valid @ModelAttribute("instrExamInfoCommand") InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_UPDATE_PAGE;
        }
        this.instrumentsExaminationService.updateInstrExam(instrumentsExaminationInfoCommand);
        return "redirect:/current-patient/visit-data/instr-exam/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteExam(@RequestParam("id") Long id, Model model) {
        this.instrumentsExaminationService.deleteInstrExam(id);
        model.addAttribute("examDeleted", Boolean.TRUE);
        return this.viewSection(model);
    }

    @GetMapping
    @RequestMapping("/view")
    public String viewExam(@RequestParam("id") Long id, Model model) {
        model.addAttribute("instrExamInfoCommand", this.instrumentsExaminationService.retrieveInstrExamById(id));
        return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_VIEW;
    }
}
