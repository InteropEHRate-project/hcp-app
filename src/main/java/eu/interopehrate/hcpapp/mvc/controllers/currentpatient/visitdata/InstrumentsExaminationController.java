package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.InstrumentsExaminationEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.InstrumentsExaminationService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.validation.Valid;
import java.util.List;

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
        model.addAttribute("instrumentExaminationList", this.instrumentsExaminationService.listNewInstrumentExamination());
        model.addAttribute("resultList", this.instrumentsExaminationService.instrExam().getListOfResultNote());
        model.addAttribute("instrExamInfoCommand", new InstrumentsExaminationInfoCommand());
        return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_VIEW_PAGE;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("resultList", this.instrumentsExaminationService.instrExam().getListOfResultNote());
        model.addAttribute("instrExamInfoCommand", new InstrumentsExaminationInfoCommand());
        return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_ADD_PAGE;
    }

    @SneakyThrows
    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@RequestParam("fileContent") MultipartFile fileContent, @Valid @ModelAttribute InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand, BindingResult bindingResult, String resultList) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_INSTRUMENTS_EXAM_ADD_PAGE;
        }
        instrumentsExaminationInfoCommand.setData(fileContent.getBytes());
        this.instrumentsExaminationService.insertResultNote(resultList);
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

    @Bean(name = "multipart")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000000);
        return multipartResolver;
    }

    @SneakyThrows
    @PostMapping("/choose")
    public String handleFileUpload(@RequestParam("files") MultipartFile[] files, Model model) {
        List<InstrumentsExaminationEntity> filesDB = instrumentsExaminationService.getFiles();
        model.addAttribute("file", filesDB);
        for (MultipartFile file : files) {
            instrumentsExaminationService.store(file);
        }
        return "redirect:/current-patient/visit-data/instr-exam/view-section";
    }

    @PostMapping
    @RequestMapping("/save")
    public String saveResultNote(String resultList) {
        this.instrumentsExaminationService.insertResultNote(resultList);
        return "redirect:/current-patient/visit-data/instr-exam/view-section";
    }
}
