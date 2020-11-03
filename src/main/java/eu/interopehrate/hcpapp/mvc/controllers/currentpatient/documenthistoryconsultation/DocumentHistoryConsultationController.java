package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.documenthistoryconsultation;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/document-history-consultation")
public class DocumentHistoryConsultationController {
    private DocumentHistoryConsultationService documentHistoryConsultationService;

    public DocumentHistoryConsultationController(DocumentHistoryConsultationService historyConsultationService) {
        this.documentHistoryConsultationService = historyConsultationService;
    }

    @GetMapping
    @RequestMapping("/view-section/{speciality}/{date}")
    public String viewSection(Model model,
                              @PathVariable(value = "speciality") String speciality,
                              @PathVariable(value = "date") String date,
                              String start, String end, HttpSession session) {

        List<DocumentHistoryConsultationInfoCommand> docHisList;
        if ((start == null && end == null) || (start.equalsIgnoreCase("") && end.equalsIgnoreCase(""))) {
            docHisList = this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality).getDocumentHistoryConsultationInfoCommandList();
            docHisList = this.documentHistoryConsultationService.filterByDate(docHisList, date);
        } else {
            try {
                docHisList = this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality).getDocumentHistoryConsultationInfoCommandList();
                docHisList = this.documentHistoryConsultationService.filterBetween(docHisList, start, end);
            }
            catch (NumberFormatException e) {
                docHisList = this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality).getDocumentHistoryConsultationInfoCommandList();
                docHisList = this.documentHistoryConsultationService.filterByDate(docHisList, date);
            }
        }
        if (Objects.nonNull(start) && Objects.nonNull(end)  && (start.equalsIgnoreCase("") && end.equalsIgnoreCase(""))) {
            date = "all";
        }
        model.addAttribute("documentHistoryConsultationList", docHisList);
        model.addAttribute("isFiltered", this.documentHistoryConsultationService.isFiltered());
        model.addAttribute("isEmpty", this.documentHistoryConsultationService.isEmpty());
        session.setAttribute("speciality", speciality);
        session.setAttribute("date", date);
        session.setAttribute("startDate", start);
        session.setAttribute("endDate", end);
        return TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION;
    }
}
