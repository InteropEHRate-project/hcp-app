package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.historyprocedure;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/history-procedure/procedure")
public class HistoryProcedureProcedureController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_HISTORY_PROCEDURE_PROCEDURE_VIEW_SECTION;
    }
}