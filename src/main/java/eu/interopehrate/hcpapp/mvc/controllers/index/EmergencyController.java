package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/index")
public class EmergencyController {
    private final IndexService indexService;

    public EmergencyController(IndexService indexService) {
        this.indexService = indexService;
    }

    @RequestMapping("/emergency")
    public String indexTemplate(HttpSession session, Model model) throws Exception {
        if (Objects.nonNull(session.getAttribute("isWorking"))) {
            session.removeAttribute("isWorking");
        }
        model.addAttribute("index", indexService.indexCommand());
        return TemplateNames.INDEX_EMERGENCY;
    }
}
