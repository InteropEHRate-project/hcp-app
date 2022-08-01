package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticImaging;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/diagnostic-imaging/file-view")
public class DiagnosticFileViewController {

    @GetMapping
    @RequestMapping("/show-document/{nameDiagnostic}/{dateOfReport}")
    public String showDocument(Model model,
                               @PathVariable(value = "nameDiagnostic") String nameDiagnostic) {
        model.addAttribute("dataString", DiagnosticImagingController.imageCommand.find(nameDiagnostic).getCompleteStringForDiagnosticReportDisplaying());
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_IMAGING_IMAGE_REPORT_VIEW_FILE;
    }

    @GetMapping
    @RequestMapping("/show-document-new-tab/{nameDiagnostic}/{dateOfReport}")
    public String showDocumentInNewTab(Model model,
                                       @PathVariable(value = "nameDiagnostic") String nameDiagnostic) {
        model.addAttribute("dataString", DiagnosticImagingController.imageCommand.find(nameDiagnostic).getCompleteStringForDiagnosticReportDisplaying());
        return TemplateNames.CURRENT_PATIENT_DIAGNOSTIC_IMAGING_IMAGE_REPORT_VIEW_FILE_NEW_TAB;
    }
}
