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
    @RequestMapping("/save")
    public String saveAdd(String reason, String finding, String procedure, String condition, String instruction) {
        this.hospitalDischargeReportService.insertReason(reason);
        this.hospitalDischargeReportService.insertFinding(finding);
        this.hospitalDischargeReportService.insertProcedure(procedure);
        this.hospitalDischargeReportService.insertCondition(condition);
        this.hospitalDischargeReportService.insertInstruction(instruction);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-reason")
    public String deleteReason(@RequestParam("reason") String reason) {
        this.hospitalDischargeReportService.deleteReason(reason);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-finding")
    public String deleteFinding(@RequestParam("finding") String finding) {
        this.hospitalDischargeReportService.deleteFinding(finding);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-procedure")
    public String deleteProcedure(@RequestParam("procedure") String procedure) {
        this.hospitalDischargeReportService.deleteProcedure(procedure);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-condition")
    public String deleteCondition(@RequestParam("condition") String condition) {
        this.hospitalDischargeReportService.deleteCondition(condition);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-instruction")
    public String deleteInstruction(@RequestParam("instruction") String instruction) {
        this.hospitalDischargeReportService.deleteInstruction(instruction);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }
}
