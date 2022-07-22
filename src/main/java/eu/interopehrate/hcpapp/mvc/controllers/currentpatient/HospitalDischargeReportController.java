package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.pdfdownload.PDFController;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/current-patient/hospital-discharge-report")
public class HospitalDischargeReportController {
    private final HospitalDischargeReportService hospitalDischargeReportService;
    private final PDFController pdfController;

    public HospitalDischargeReportController(HospitalDischargeReportService hospitalDischargeReportService, PDFController pdfController) {
        this.hospitalDischargeReportService = hospitalDischargeReportService;
        this.pdfController = pdfController;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("hospitalDischargeReportCommand", this.hospitalDischargeReportService.hospitalDischargeReportCommand());
        model.addAttribute("hasPrescriptions", !this.hospitalDischargeReportService.getPrescriptionRepository().findAll().isEmpty());
        model.addAttribute("hasVitalSigns", !this.hospitalDischargeReportService.getVitalSignsRepository().findAll().isEmpty());
        model.addAttribute("hasCurrentDiseases", !this.hospitalDischargeReportService.getCurrentDiseaseRepository().findAll().isEmpty());
        model.addAttribute("hasAllergies", !this.hospitalDischargeReportService.getAllergyRepository().findAll().isEmpty());
        model.addAttribute("hasDiagnosticConclusion", !this.hospitalDischargeReportService.getDiagnosticConclusionRepository().findAll().isEmpty());
        model.addAttribute("hasPhExam", !this.hospitalDischargeReportService.getPhExamRepository().findAll().isEmpty());
        model.addAttribute("hasLaboratory", !this.hospitalDischargeReportService.getLaboratoryTestsRepository().findAll().isEmpty());
        model.addAttribute("hasReason", !this.hospitalDischargeReportService.getReasonRepository().findAll().isEmpty());
        return TemplateNames.CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT;
    }

    @PostMapping
    @RequestMapping("/save")
    public String saveAdd(@ModelAttribute("hospitalDischargeReportCommand") HospitalDischargeReportCommand hospitalDischargeReportCommand) {
        this.hospitalDischargeReportService.insertDetails(hospitalDischargeReportCommand);
        return "redirect:/current-patient/hospital-discharge-report/view-section";
    }

    @GetMapping
    @RequestMapping("/save-in-cloud")
    public String saveInCloud(Model model, HttpServletRequest request, HttpServletResponse response) {
        byte[] content = this.pdfController.retrievePdfAsBytes(request, response);
        model.addAttribute("savedToCloud", this.hospitalDischargeReportService.saveInCloud(content));
        return this.viewSection(model);
    }
}
