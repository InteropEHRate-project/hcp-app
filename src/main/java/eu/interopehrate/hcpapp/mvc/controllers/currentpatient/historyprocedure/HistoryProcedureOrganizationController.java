package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.historyprocedure;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/history-procedure/organization")
public class HistoryProcedureOrganizationController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_HISTORY_PROCEDURE_ORGANIZATION_VIEW_SECTION;
    }
}
