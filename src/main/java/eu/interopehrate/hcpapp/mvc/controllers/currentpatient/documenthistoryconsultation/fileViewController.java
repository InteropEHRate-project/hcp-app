package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.documenthistoryconsultation;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/document-history-consultation/file-view")
public class fileViewController {

    @GetMapping
    @RequestMapping("/show-document/{exam}/{date}")
    public String showDocument(Model model,
                               @PathVariable(value = "exam") String exam,
                               @PathVariable(value = "date") String date) {
        model.addAttribute("dataString", DocumentHistoryConsultationController.docHisCommand.find(exam, date).getDataCompleteText());
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_FILE;
    }

    @GetMapping
    @RequestMapping("/show-document-new-tab/{exam}/{date}")
    public String showDocumentInNewTab(Model model,
                                       @PathVariable(value = "exam") String exam,
                                       @PathVariable(value = "date") String date) {
        model.addAttribute("dataString", DocumentHistoryConsultationController.docHisCommand.find(exam, date).getDataCompleteText());
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_FILE_NEW_TAB;
    }
}
