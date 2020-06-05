package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@Scope("session")
public class IndexController {
    private IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @RequestMapping({"/", "/index"})
    public String indexTemplate(Model model, HttpSession session) throws Exception {
        model.addAttribute("index", indexService.indexCommand());
        session.setAttribute("mySessionAttribute", indexService.indexCommand());
        return TemplateNames.INDEX_TEMPLATE;
    }

    @RequestMapping("/index/open-connection")
    public String openConnection() {
        indexService.openConnection();
        return "redirect:/index";
    }

    @RequestMapping("/index/close-connection")
    public String closeConnection() {
        indexService.closeConnection();
        return "redirect:/index";
    }

    @RequestMapping("/index/stop-listening")
    public String stopListening() {
        return "redirect:/index";
    }
}
