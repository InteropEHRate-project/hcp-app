package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/demographic-information")
public class DemographicInformationController {
    private IndexService indexService;

    public DemographicInformationController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) throws Exception {
        model.addAttribute("index", indexService.indexCommand());
        return TemplateNames.CURRENT_PATIENT_DEMOGRAPHIC_INFORMATION_VIEW_SECTION;
    }
}
