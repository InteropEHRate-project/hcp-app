package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticImaging;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;
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
    public static ImageCommand imageCommand;

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
        imageCommand = this.diagnosticImagingService.imageCommand();
        model.addAttribute("imageCommand", imageCommand);
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

    @GetMapping
    @RequestMapping("/refresh-data")
    public String refreshData() {
        this.diagnosticImagingService.refreshData();
        return "redirect:/current-patient/diagnostic-imaging/view-section";
    }
}
