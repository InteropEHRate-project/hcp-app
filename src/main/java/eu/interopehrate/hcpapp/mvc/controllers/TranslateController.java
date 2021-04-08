package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TranslateController {
    private final CurrentPatient currentPatient;

    public TranslateController(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @GetMapping
    @RequestMapping("/language")
    public String english(@RequestParam(name = "lang") String lang) {
        if (lang.equals("en")) {
            currentPatient.setDisplayTranslatedVersion(Boolean.TRUE);
        } else if (lang.equals("it")) {
            currentPatient.setDisplayTranslatedVersion(Boolean.FALSE);
        }
        return "redirect:/index";
    }
}
