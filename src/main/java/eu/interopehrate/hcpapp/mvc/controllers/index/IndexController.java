package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@Scope("session")
public class IndexController {
    private final IndexService indexService;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public IndexController(IndexService indexService, HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.indexService = indexService;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @GetMapping
    @RequestMapping({"/", "/index"})
    public String indexTemplate(Model model, HttpSession session) throws Exception {
        model.addAttribute("index", indexService.indexCommand());
        IndexCommand indexCommand = indexService.indexCommand();
        if (Objects.isNull(session.getAttribute("patientNavbar")) && IndexCommand.transmissionCompleted) {
            session.setAttribute("patientNavbar", indexCommand);
        }
        if (Objects.isNull(session.getAttribute("hcpName")) && Objects.nonNull(this.healthCareProfessionalRepository)) {
            session.setAttribute("hcpName",
                    this.healthCareProfessionalRepository.findAll().get(0).getFirstName() + " " + this.healthCareProfessionalRepository.findAll().get(0).getLastName());
        }
        if (Objects.nonNull(session.getAttribute("itWorked"))) {
            session.removeAttribute("itWorked");
        }
        if (Objects.nonNull(this.indexService.getCurrentPatient())) {
            session.setAttribute("withoutConnection", this.indexService.getCurrentPatient().getWithoutConnection());
        }
        return TemplateNames.INDEX_TEMPLATE;
    }

    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return TemplateNames.ACCESS_DENIED;
    }

    @RequestMapping("/index/open-connection")
    public String openConnection() {
        indexService.openConnection();
        return "redirect:/index";
    }

    @RequestMapping("/index/close-connection")
    public String closeConnection(HttpSession session) {
        indexService.closeConnection();
        if (Objects.nonNull(session.getAttribute("patientNavbar"))) {
            session.removeAttribute("patientNavbar");
        }
        if (Objects.nonNull(session.getAttribute("alreadyAdded"))) {
            session.removeAttribute("alreadyAdded");
        }
        if (Objects.nonNull(session.getAttribute("workingSession"))) {
            session.removeAttribute("workingSession");
        }
        return "redirect:/index/close-cloud-connection";
    }

    @RequestMapping("/index/stop-listening")
    public String stopListening(HttpSession session) {
        indexService.closeConnection();
        if (Objects.nonNull(session.getAttribute("workingSession"))) {
            session.removeAttribute("workingSession");
        }
        return "redirect:/index";
    }
}
