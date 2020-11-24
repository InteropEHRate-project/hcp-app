package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@Scope("session")
public class IndexController {
    private IndexService indexService;
    private String username;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
        Object principal = null;
        if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        if(principal instanceof UserDetails) {
            this.username = ((UserDetails) principal).getUsername();
        } else if (Objects.nonNull(principal)) {
            this.username = principal.toString();
        } else {
            this.username = null;
        }
    }

    @RequestMapping({"/", "/index"})
    public String indexTemplate(Model model, HttpSession session) throws Exception {
        model.addAttribute("index", indexService.indexCommand());
        session.setAttribute("mySessionAttribute", indexService.indexCommand());
        session.setAttribute("username", this.username);
        return TemplateNames.INDEX_TEMPLATE;
    }

    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return TemplateNames.ACCESS_DENIED;
    }

    @RequestMapping("/index/open-connection")
    public String openConnection() {
        indexService.openConnection();
        return "redirect:/index";
    }

    @RequestMapping("/index/close-connection")
    public String closeConnection() {
        indexService.closeConnection();
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
