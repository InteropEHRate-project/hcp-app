package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.documenthistoryconsultation;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/document-history-consultation")
public class DocumentHistoryConsultationController {
    private final DocumentHistoryConsultationService documentHistoryConsultationService;

    public DocumentHistoryConsultationController(DocumentHistoryConsultationService historyConsultationService) {
        this.documentHistoryConsultationService = historyConsultationService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        DocumentHistoryConsultationCommand documentHistoryConsultationCommand = this.documentHistoryConsultationService.getData();
        model.addAttribute("currentD2DConnection", this.documentHistoryConsultationService.getCurrentD2DConnection());
        model.addAttribute("documentHistoryConsultationCommand", documentHistoryConsultationCommand);
        if (Objects.nonNull(documentHistoryConsultationCommand)) {
            model.addAttribute("documentHistoryConsultationList", documentHistoryConsultationCommand.getDocumentHistoryConsultationInfoCommandList());
        }
        model.addAttribute("isFiltered", this.documentHistoryConsultationService.isFiltered());
        model.addAttribute("isEmpty", this.documentHistoryConsultationService.isEmpty());
        model.addAttribute("lastYear", LocalDate.now().getYear() - 1);
        model.addAttribute("last5Years", LocalDate.now().getYear() - 5);
        model.addAttribute("now", LocalDate.now());
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION;
    }

    @PostMapping
    @RequestMapping("/filter")
    public String filter(Model model, HttpSession session,
                         String speciality,
                         String date,
                         String start, String end) throws Exception {
        session.setAttribute("speciality", speciality);
        session.setAttribute("date", date);
        session.setAttribute("startDate", start);
        session.setAttribute("endDate", end);
        model.addAttribute("documentHistoryConsultationCommand", this.documentHistoryConsultationService.getData());
        model.addAttribute("currentD2DConnection", this.documentHistoryConsultationService.getCurrentD2DConnection());
        model.addAttribute("documentHistoryConsultationList",
                this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality, date, start, end).getDocumentHistoryConsultationInfoCommandList());
        model.addAttribute("isFiltered", this.documentHistoryConsultationService.isFiltered());
        model.addAttribute("isEmpty", this.documentHistoryConsultationService.isEmpty());
        model.addAttribute("lastYear", LocalDate.now().getYear() - 1);
        model.addAttribute("last5Years", LocalDate.now().getYear() - 5);
        model.addAttribute("now", LocalDate.now());
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/refresh")
    public String refresh() throws Exception {
        this.documentHistoryConsultationService.refresh();
        return "redirect:/current-patient/document-history-consultation/view-section";
    }

    @GetMapping
    @RequestMapping("/refresh-data")
    public String refreshData() {
        this.documentHistoryConsultationService.refreshData();
        return "redirect:/current-patient/document-history-consultation/view-section";
    }
}
