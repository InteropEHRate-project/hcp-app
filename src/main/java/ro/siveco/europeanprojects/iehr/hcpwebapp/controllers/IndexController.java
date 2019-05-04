package ro.siveco.europeanprojects.iehr.hcpwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping({"/", "/index"})
    public String indexTemplate(){
        return TemplateNames.INDEX_TEMPLATE;
    }
}
