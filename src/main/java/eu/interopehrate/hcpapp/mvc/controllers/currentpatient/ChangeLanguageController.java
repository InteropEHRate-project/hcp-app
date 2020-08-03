package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hcp-web-ui")
public class ChangeLanguageController {

    @GetMapping
    public String hello() {
        return "Welcome";
    }
}
