package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/outpatient-report")
public class OutpatientReportController {
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;

    public OutpatientReportController(PrescriptionService prescriptionService, VitalSignsService vitalSignsService) {
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        model.addAttribute("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        return TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT;
    }
}
