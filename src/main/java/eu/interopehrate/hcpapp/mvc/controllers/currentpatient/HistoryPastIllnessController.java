package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/current-patient/history-past-illness")
public class HistoryPastIllnessController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_HISTORY_OF_PAST_ILLNESS_VIEW_SECTION;
    }
}
