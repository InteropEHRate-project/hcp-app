package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return TemplateNames.CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT;
    }

    @PostMapping
    @RequestMapping("/save-reason")
    public String saveAdd(String reason) {
        this.hospitalDischargeReportService.insertReason(reason);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteReason(@RequestParam("reason") String reason) {
        this.hospitalDischargeReportService.deleteReason(reason);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }
}
