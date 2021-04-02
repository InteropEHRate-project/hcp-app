package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.currentsession.WorkingSession;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.IndexService;
import eu.interopehrate.hcpapp.services.index.InpatientEncounterService;
import eu.interopehrate.hcpapp.services.index.impl.ContinueExistingVisitServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Controller
@Scope("session")
@RequestMapping("/index")
public class InpatientEncounterController {
    private final IndexService indexService;
    private final InpatientEncounterService inpatientEncounterService;

    public InpatientEncounterController(IndexService indexService, InpatientEncounterService inpatientEncounterService) {
        this.indexService = indexService;
        this.inpatientEncounterService = inpatientEncounterService;
    }

    @GetMapping
    @RequestMapping({"/inpatient-encounter"})
    public String indexTemplate(HttpSession session, Model model) throws Exception {
        if (Objects.nonNull(session.getAttribute("isWorking"))) {
            session.removeAttribute("isWorking");
        }
        model.addAttribute("index", indexService.indexCommand());
        boolean error = false;
        try {
            model.addAttribute("inpatientList", this.inpatientEncounterService.getListOfPatients());
        } catch (ResourceAccessException e) {
            log.error("Connection is not establish");
            error = true;
        } finally {
            model.addAttribute("error", error);
        }
        return TemplateNames.INDEX_INPATIENT_ENCOUNTER;
    }

    @GetMapping
    @RequestMapping({"/retrieve-inpatient"})
    public String retrievePatient(HttpSession session, @RequestParam(name = "patientId") String patientId) throws Exception {
        this.inpatientEncounterService.retrieveEHRs(patientId);
        session.setAttribute("isExtractedData", ContinueExistingVisitServiceImpl.isExtractedData);
        IndexCommand indexCommand = this.indexService.indexCommand();
        if (Objects.isNull(session.getAttribute("patientNavbar"))) {
            session.setAttribute("patientNavbar", indexCommand);
        }
        session.setAttribute("workingSession", WorkingSession.INPATIENT_ENCOUNTER.toString());
        return "redirect:/index";
    }
}
