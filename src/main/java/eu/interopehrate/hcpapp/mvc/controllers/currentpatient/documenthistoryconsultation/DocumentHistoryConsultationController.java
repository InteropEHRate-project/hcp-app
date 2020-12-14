package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.documenthistoryconsultation;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/current-patient/document-history-consultation")
public class DocumentHistoryConsultationController {
    private DocumentHistoryConsultationService documentHistoryConsultationService;

    public DocumentHistoryConsultationController(DocumentHistoryConsultationService historyConsultationService) {
        this.documentHistoryConsultationService = historyConsultationService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model, HttpSession session,
                              String speciality,
                              String date,
                              String start, String end) throws Exception {
        DocumentHistoryConsultationCommand docHisCommand = this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality, date, start, end);

        session.setAttribute("speciality", speciality);
        session.setAttribute("date", date);
        session.setAttribute("startDate", start);
        session.setAttribute("endDate", end);
        model.addAttribute("currentD2DConnection", this.documentHistoryConsultationService.getCurrentD2DConnection());
        model.addAttribute("documentHistoryConsultationList", docHisCommand.getDocumentHistoryConsultationInfoCommandList());
        model.addAttribute("isFiltered", this.documentHistoryConsultationService.isFiltered());
        model.addAttribute("isEmpty", this.documentHistoryConsultationService.isEmpty());
        model.addAttribute("lastYear", LocalDate.now().getYear() - 1);
        model.addAttribute("last5Years", LocalDate.now().getYear() - 5);
        model.addAttribute("now", LocalDate.now());
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION;
    }
}
