package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticImaging;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticImagingService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

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
    public String imageReportSection(Model model, HttpSession session) {
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("emergency", CurrentPatient.typeOfWorkingSession.toString());
        }
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

    @SneakyThrows
    @GetMapping
    @RequestMapping("/store-media-file")
    public String storeMediaFile(String mediaId) {
        List<ImageInfoCommand> imageInfoCommandList = this.diagnosticImagingService.imageCommand().getImageInfoCommands();
        for (ImageInfoCommand item : imageInfoCommandList) {
            if (item.getImageName().equals(mediaId)) {
                this.diagnosticImagingService.downloadMediaFile(item.getImageContent(), item.getImageName(), item.getImageType());
            }
        }
        return "redirect:/current-patient/diagnostic-imaging/image-report";
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
        return "redirect:/current-patient/diagnostic-imaging/image-report";
    }

    @GetMapping
    @RequestMapping("/refresh")
    public String refresh() throws Exception {
        this.diagnosticImagingService.refresh();
        return "redirect:/current-patient/diagnostic-imaging/image-report";
    }
}
