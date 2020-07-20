package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Scope("session")
public class IndexController {
    private IndexService indexService;
    private PrescriptionService prescriptionService;

    public IndexController(IndexService indexService, PrescriptionService prescriptionService) {
        this.indexService = indexService;
        this.prescriptionService = prescriptionService;
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
        prescriptionService.clearUploadedList();
        return "redirect:/index";
    }

    @RequestMapping("/index/stop-listening")
    public String stopListening() {
        return "redirect:/index";
    }

    @RequestMapping("/index/close-cloud-connection")
    public String closeCloudConnection() {
        indexService.closeCloudConnection();
        return "redirect:/index";
    }

    @PostMapping
    @RequestMapping("/index/open-cloud-connection")
    public String openCloudConnection() {
        indexService.openCloudConnection();
        return "redirect:/index";
    }

    @PostMapping
    @RequestMapping("/index/discard-cloud-connection")
    public String discardCloudConnection() {
        indexService.discardCloudConnection();
        return "redirect:/index";
    }

    @PostMapping
    @RequestMapping("/index/download-ips")
    public String downloadIps(@Valid @ModelAttribute IndexCommand indexCommand, BindingResult bindingResult, Model model) throws Exception{
        if (bindingResult.hasErrors()) {
            if (indexCommand.getCloudConnectionState() == null) {
                indexCommand.setCloudConnectionState(CloudConnectionState.OFF);
            }
            model.addAttribute("index", indexService.indexCommand());
            return TemplateNames.INDEX_TEMPLATE;
        }
        this.indexService.downloadCloudIps(indexCommand.getQrCode());
        return "redirect:/index";
    }
}
