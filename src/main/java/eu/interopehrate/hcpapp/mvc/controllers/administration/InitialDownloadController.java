package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.commands.SEHRInitialDownloadCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.SEHRInitialDownloadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/initial-download")
public class InitialDownloadController {
    private SEHRInitialDownloadService sehrInitialDownloadService;

    public InitialDownloadController(SEHRInitialDownloadService sehrInitialDownloadService) {
        this.sehrInitialDownloadService = sehrInitialDownloadService;
    }

    @GetMapping
    @RequestMapping("/from-sehr")
    public String getSEHRInitial(Model model) {
        SEHRInitialDownloadCommand sehrInitialDownloadCommand = sehrInitialDownloadService.getInitialConfig();
        model.addAttribute("sehrInitialDownload", sehrInitialDownloadCommand);
        return TemplateNames.ADMINISTRATION_INITIAL_DOWNLOAD_FROM_SEHR;
    }

    @PostMapping
    @RequestMapping("/save-sehr-initial")
    public String saveSEHRInitial(@ModelAttribute SEHRInitialDownloadCommand command){
        return "redirect:/administration/initial-download/from-sehr";
    }
}
