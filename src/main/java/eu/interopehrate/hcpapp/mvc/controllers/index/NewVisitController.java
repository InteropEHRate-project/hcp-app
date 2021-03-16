package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/index")
public class NewVisitController {
    private final IndexService indexService;

    public NewVisitController(IndexService indexService) {
        this.indexService = indexService;
    }

    @RequestMapping("/new-patient")
    public String indexTemplate(Model model, HttpSession session) throws Exception {
        if (Objects.nonNull(session.getAttribute("isWorking"))) {
            session.removeAttribute("isWorking");
        }
        model.addAttribute("index", indexService.indexCommand());
        IndexCommand indexCommand = indexService.indexCommand();
        if (Objects.isNull(session.getAttribute("patientNavbar")) && IndexCommand.transmissionCompleted) {
            session.setAttribute("patientNavbar", indexCommand);
        }
        return TemplateNames.INDEX_NEW_PATIENT;
    }

    @RequestMapping("/new-patient/open-connection")
    public String openConnection() {
        indexService.openConnection();
        return "redirect:/index/new-patient";
    }
}
