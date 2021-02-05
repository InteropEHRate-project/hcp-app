package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticImagingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-imaging")
public class DiagnosticImagingController {
    private final DiagnosticImagingService diagnosticImagingService;

    public DiagnosticImagingController(DiagnosticImagingService diagnosticImagingService) {
        this.diagnosticImagingService = diagnosticImagingService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_IMAGING_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/DICOM")
    public String dicomSection() {
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_IMAGING_DICOM;
    }

    @GetMapping
    @RequestMapping("/image-report")
    public String imageReportSection(Model model) {
        model.addAttribute("imageCommand", this.diagnosticImagingService.imageCommand());
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_IMAGING_IMAGE_REPORT;
    }

    @GetMapping
    @RequestMapping("/view-ecg")
    public String viewEcg() {
        this.diagnosticImagingService.displayEcgDemo();
        return "redirect:/current-patient/diagnostic-imaging/DICOM";
    }

    @GetMapping
    @RequestMapping("/view-mr")
    public String viewMr() {
        this.diagnosticImagingService.displayMrDemo();
        return "redirect:/current-patient/diagnostic-imaging/DICOM";
    }
}
