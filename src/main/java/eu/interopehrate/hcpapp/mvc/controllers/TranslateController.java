package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TranslateController {
    private final CurrentPatient currentPatient;

    public TranslateController(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @GetMapping
    @RequestMapping("/language")
    public String english(@RequestParam(name = "lang") String lang, HttpServletRequest httpServletRequest) {
        if (lang.equals("en")) {
            this.currentPatient.setDisplayTranslatedVersion(Boolean.TRUE);
        } else if (lang.equals("it")) {
            this.currentPatient.setDisplayTranslatedVersion(Boolean.FALSE);
        }
        try {
            return "redirect:" + httpServletRequest.getHeader("referer");
        } catch (Exception e) {
            return "redirect:/index";
        }
    }
}
