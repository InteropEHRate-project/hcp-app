package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@Scope("session")
@RequestMapping("/index")
public class ContinueExistingVisitController {
    private final RestTemplate restTemplate;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;

    public ContinueExistingVisitController(RestTemplate restTemplate, HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.restTemplate = restTemplate;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @GetMapping
    @RequestMapping({"/existing-visit"})
    public String indexTemplate(Model model) {
        List<HealthCareProfessionalEntity> all = this.healthCareProfessionalRepository.findAll();
        model.addAttribute("existingVisit", restTemplate.postForObject(this.hospitalServicesUrl + "/patients" + "/list", all.get(0).getId(), List.class));
        return TemplateNames.INDEX_EXISTING_VISIT;
    }
}
