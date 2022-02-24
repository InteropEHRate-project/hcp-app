package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticConclusionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/current-patient/visit-data/conclusion")
public class DiagnosticConclusionController {
    private final DiagnosticConclusionService diagnosticConclusionService;

    public DiagnosticConclusionController(DiagnosticConclusionService diagnosticConclusionService) {
        this.diagnosticConclusionService = diagnosticConclusionService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("conclusionCommand", this.diagnosticConclusionService.conclusionComm());
        model.addAttribute("conclusionNote", this.diagnosticConclusionService.conclusionComm().getListOfConclusionNote());
        model.addAttribute("treatmentPlan", this.diagnosticConclusionService.conclusionComm().getListOfTreatmentPlan());
        model.addAttribute("infoConclusion", new DiagnosticConclusionInfoCommand());
        model.addAttribute("newTreatmentConclusion", this.diagnosticConclusionService.getNewConclusion());
        return TemplateNames.CURRENT_PATIENT_CONCLUSION_VIEW_SECTION;
    }

    @PostMapping
    @RequestMapping("/save")
    public String saveConclusionNote(String conclusionNote, String treatmentPlan, @ModelAttribute DiagnosticConclusionInfoCommand diagnosticConclusionInfoCommand) {
        this.diagnosticConclusionService.insertConclusionNote(conclusionNote);
        this.diagnosticConclusionService.insertTreatmentPlan(treatmentPlan);
        this.diagnosticConclusionService.insertTreatment(diagnosticConclusionInfoCommand);
        return "redirect:/current-patient/visit-data/conclusion/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String delete(@RequestParam("note") String note) {
        this.diagnosticConclusionService.deleteNote(note);
        return "redirect:/current-patient/visit-data/conclusion/view-section";
    }
}
