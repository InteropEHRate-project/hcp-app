package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/hospital-discharge-report")
public class HospitalDischargeReportController {
    private final HospitalDischargeReportService hospitalDischargeReportService;

    public HospitalDischargeReportController(HospitalDischargeReportService hospitalDischargeReportService) {
        this.hospitalDischargeReportService = hospitalDischargeReportService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("hospitalDischargeReportCommand", this.hospitalDischargeReportService.hospitalDischargeReportCommand());
        model.addAttribute("hasPrescriptions", !this.hospitalDischargeReportService.getPrescriptionRepository().findAll().isEmpty());
        model.addAttribute("hasVitalSigns", !this.hospitalDischargeReportService.getVitalSignsRepository().findAll().isEmpty());
        return TemplateNames.CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT;
    }

    @PostMapping
    @RequestMapping("/save")
    public String saveAdd(@ModelAttribute("hospitalDischargeReportCommand") HospitalDischargeReportCommand hospitalDischargeReportCommand) {
        this.hospitalDischargeReportService.insertDetails(hospitalDischargeReportCommand);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }
}
