package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/index")
public class EmergencyController {
    private final IndexService indexService;

    public EmergencyController(IndexService indexService) {
        this.indexService = indexService;
    }

    @RequestMapping("/emergency")
    public String indexTemplate(HttpSession session, Model model) throws Exception {
        if (Objects.nonNull(session.getAttribute("isWorking"))) {
            session.removeAttribute("isWorking");
        }
        model.addAttribute("index", this.indexService.indexCommand());
        return TemplateNames.INDEX_EMERGENCY;
    }

    @GetMapping
    @RequestMapping("/open-cloud-connection")
    public String openCloudConnection() {
        this.indexService.openCloudConnection();
        return "redirect:/index/emergency";
    }

    @GetMapping
    @RequestMapping("/access-cloud")
    public String requestAccess(String qrCodeContent, String hospitalID) throws Exception {
        this.indexService.requestAccess(qrCodeContent, hospitalID);
        return "redirect:/index/emergency";
    }

    @GetMapping
    @RequestMapping("/close-cloud-connection")
    public String closeCloudConnection() {
        this.indexService.closeCloudConnection();
        return "redirect:/index";
    }

    @GetMapping
    @RequestMapping("/discard-cloud-connection")
    public String discardCloudConnection() {
        this.indexService.discardCloudConnection();
        return "redirect:/index";
    }

    @PostMapping
    @RequestMapping("/download-ips")
    public String downloadIps(@Valid @ModelAttribute IndexCommand indexCommand, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            if (indexCommand.getCloudConnectionState() == null) {
                indexCommand.setCloudConnectionState(CloudConnectionState.OFF);
            }
            return "redirect:/index/emergency";
        }
        Boolean itWorked = this.indexService.retrieveData(indexCommand.getQrCode(), indexCommand.getHospitalID());
        session.setAttribute("itWorked", itWorked);
        if (itWorked) {
            return "redirect:/index";
        }
        return this.openCloudConnection();
    }
}
