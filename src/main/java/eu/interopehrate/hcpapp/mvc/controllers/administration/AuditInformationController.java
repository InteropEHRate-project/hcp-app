package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/audit-information")
public class AuditInformationController {
    private final AuditInformationService auditInformationService;

    public AuditInformationController(AuditInformationService auditInformationService) {
        this.auditInformationService = auditInformationService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsAudit(Model model) {
        model.addAttribute("auditInformation", auditInformationService.getAuditInformationCommand());
        return TemplateNames.ADMINISTRATION_AUDIT_INFORMATION_VIEW_DETAILS;
    }
}
