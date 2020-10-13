package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/current-patient/document-history-consultation")
public class DocumentHistoryConsultationController {
    private DocumentHistoryConsultationService documentHistoryConsultationService;

    public DocumentHistoryConsultationController(DocumentHistoryConsultationService historyConsultationService) {
        this.documentHistoryConsultationService = historyConsultationService;
    }

    @GetMapping
    @RequestMapping("/view-section/{speciality}")
    public String viewSection(Model model, @PathVariable(value = "speciality") String speciality) {
        model.addAttribute("documentHistoryConsultation", documentHistoryConsultationService.documentHistoryConsultationCommand(speciality));
        model.addAttribute("isFiltered", this.documentHistoryConsultationService.isFiltered());
        model.addAttribute("isEmpty", this.documentHistoryConsultationService.isEmpty());
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/pdf/{speciality}/{exam}/{date}")
    public OutputStream pdf(@PathVariable(value = "speciality") String speciality,
                            @PathVariable(value = "exam") String exam,
                            @PathVariable(value = "date")String date) throws IOException {
        OutputStream out = new FileOutputStream("documentHistoryConsultation.pdf");
        DocumentHistoryConsultationInfoCommand doc = this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality).find(exam, date);
        out.write(doc.getData());
        out.close();
        return out;
    }
}
