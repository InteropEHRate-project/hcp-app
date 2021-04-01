package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.currentsession.WorkingSession;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.ContinueExistingVisitService;
import eu.interopehrate.hcpapp.services.index.IndexService;
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
public class ContinueExistingVisitController {
    private final ContinueExistingVisitService continueExistingVisitService;
    private final IndexService indexService;

    public ContinueExistingVisitController(ContinueExistingVisitService continueExistingVisitService, IndexService indexService) {
        this.continueExistingVisitService = continueExistingVisitService;
        this.indexService = indexService;
    }

    @GetMapping
    @RequestMapping({"/existing-visit"})
    public String indexTemplate(HttpSession session, Model model) throws Exception {
        if (Objects.nonNull(session.getAttribute("isWorking"))) {
            session.removeAttribute("isWorking");
        }
        model.addAttribute("index", indexService.indexCommand());
        boolean error = false;
        try {
            model.addAttribute("existingVisit", this.continueExistingVisitService.getListOfPatients());
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
    public String retrievePatient(HttpSession session, @RequestParam(name = "patientId") String patientId) throws Exception {
        this.continueExistingVisitService.retrieveEHRs(patientId);
        session.setAttribute("isExtractedData", ContinueExistingVisitServiceImpl.isExtractedData);
        IndexCommand indexCommand = indexService.indexCommand();
        if (Objects.isNull(session.getAttribute("patientNavbar"))) {
            session.setAttribute("patientNavbar", indexCommand);
        }
        session.setAttribute("workingSession", WorkingSession.OUTPATIENT_VISIT.toString());
        return "redirect:/index";
    }

    @GetMapping
    @RequestMapping({"/clear-data"})
    public String clearData(HttpSession session) {
        continueExistingVisitService.clearData();
        session.setAttribute("isExtractedData", ContinueExistingVisitServiceImpl.isExtractedData);
        if (Objects.nonNull(session.getAttribute("patientNavbar"))) {
            session.removeAttribute("patientNavbar");
        }
        if (Objects.nonNull(session.getAttribute("alreadyAdded"))) {
            session.removeAttribute("alreadyAdded");
        }
        if (Objects.nonNull(session.getAttribute("workingSession"))) {
            session.removeAttribute("workingSession");
        }
        return "redirect:/index";
    }
}
