package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.services.currentpatient.CurrentPatientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/current-patient")
public class SwitchLanguageController {
    private CurrentPatientService currentPatientService;

    public SwitchLanguageController(CurrentPatientService currentPatientService) {
        this.currentPatientService = currentPatientService;
    }

    @GetMapping
    @RequestMapping("/switch-display-language")
    public String switchLanguage(@RequestParam(name = "displayTranslatedVersion") String displayTranslatedVersion,
                                 @RequestParam(name = "redirect-to") String redirectTo) {
        currentPatientService.setDisplayTranslatedVersion(Boolean.valueOf(displayTranslatedVersion));
        return "redirect:" + redirectTo;
    }
}
