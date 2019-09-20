package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.MedicationSummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/current-patient/medication-summary")
public class MedicationSummaryController {
    private MedicationSummaryService medicationSummaryService;

    public MedicationSummaryController(MedicationSummaryService medicationSummaryService) {
        this.medicationSummaryService = medicationSummaryService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        List<MedicationSummaryInfoCommand> medicationSummaryInfo = medicationSummaryService.medicationSummarySection();
        model.addAttribute("medicationSummary", new MedicationSummaryCommand(medicationSummaryInfo));
        return TemplateNames.CURRENT_PATIENT_MEDICATION_SUMMARY_VIEW_SECTION;
    }
}
