package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.ContinueExistingVisitService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import eu.interopehrate.hcpapp.services.index.impl.ContinueExistingVisitServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@Scope("session")
@RequestMapping("/index")
public class ContinueExistingVisitController {
    private final RestTemplate restTemplate;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    private final ContinueExistingVisitService continueExistingVisitService;
    private final IndexService indexService;

    public ContinueExistingVisitController(RestTemplate restTemplate, HealthCareProfessionalRepository healthCareProfessionalRepository,
                                           ContinueExistingVisitService continueExistingVisitService, IndexService indexService) {
        this.restTemplate = restTemplate;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
        this.continueExistingVisitService = continueExistingVisitService;
        this.indexService = indexService;
    }

    @GetMapping
    @RequestMapping({"/existing-visit"})
    public String indexTemplate(Model model) throws Exception {
        model.addAttribute("index", indexService.indexCommand());
        boolean error = false;
        try {
            List<HealthCareProfessionalEntity> all = this.healthCareProfessionalRepository.findAll();
            model.addAttribute("existingVisit", restTemplate.postForObject(this.hospitalServicesUrl + "/patients" + "/list", all.get(0).getId(), List.class));
        } catch (ResourceAccessException e) {
            log.error("Connection is not establish");
            error = true;
        } finally {
            model.addAttribute("error", error);
        }
        return TemplateNames.INDEX_EXISTING_VISIT;
    }

    @GetMapping
    @RequestMapping({"/retrieve-patient"})
    public String retrievePatient(Model model, HttpSession session, @RequestParam(name = "patientId") String patientId) throws Exception {
        this.continueExistingVisitService.retrieveEHRs(patientId);
        session.setAttribute("isExtractedData", ContinueExistingVisitServiceImpl.isExtractedData);
        IndexCommand.transmissionCompleted = true;
        IndexCommand indexCommand = indexService.indexCommand();
        if (Objects.isNull(session.getAttribute("mySessionAttribute"))) {
            session.setAttribute("mySessionAttribute", indexCommand);
        }
        model.addAttribute("index", indexService.indexCommand());
        return "redirect:/index";
    }

    @GetMapping
    @RequestMapping({"/clear-data"})
    public String clearData(HttpSession session) {
        continueExistingVisitService.clearData();
        session.setAttribute("isExtractedData", ContinueExistingVisitServiceImpl.isExtractedData);
        IndexCommand.transmissionCompleted = false;
        if (Objects.nonNull(session.getAttribute("mySessionAttribute"))) {
            session.removeAttribute("mySessionAttribute");
        }
        return "redirect:/index";
    }
}
