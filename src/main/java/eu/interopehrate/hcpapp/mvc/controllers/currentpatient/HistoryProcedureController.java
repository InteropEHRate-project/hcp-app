package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/history-procedure")
public class HistoryProcedureController {
    @GetMapping
    @RequestMapping("/procedure/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_HISTORY_PROCEDURE_PROCEDURE_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/organization/view-section")
    public String viewSection2() {
        return TemplateNames.CURRENT_PATIENT_HISTORY_PROCEDURE_ORGANIZATION_VIEW_SECTION;
    }
}