package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @RequestMapping({"/", "/index"})
    public String indexTemplate(Model model) throws Exception {
        model.addAttribute("index", indexService.d2dConnectionState());
        return TemplateNames.INDEX_TEMPLATE;
    }
}
