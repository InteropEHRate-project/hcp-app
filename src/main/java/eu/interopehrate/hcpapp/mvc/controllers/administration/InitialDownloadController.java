package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/initial-download")
public class InitialDownloadController {

    @GetMapping
    @RequestMapping("/from-sehr")
    public String detailsTemplate() {
        return TemplateNames.ADMINISTRATION_INITIAL_DOWNLOAD_FROM_SEHR;
    }
}
