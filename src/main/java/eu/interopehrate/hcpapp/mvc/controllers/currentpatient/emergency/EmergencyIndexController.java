package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.emergency;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import eu.interopehrate.hcpapp.mvc.commands.emergency.EmergencyIndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.emergency.EmergencyIndexService;
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
public class EmergencyIndexController {
    private EmergencyIndexService emergencyIndexService;

    public EmergencyIndexController(EmergencyIndexService emergencyIndexService) {
        this.emergencyIndexService = emergencyIndexService;
    }

    @RequestMapping({"/emergency"})
    public String indexTemplate(Model model, HttpSession session) throws Exception {
        model.addAttribute("emergencyIndexCommand", emergencyIndexService.emergencyIndexCommand());
        session.setAttribute("mySessionAttribute", emergencyIndexService.emergencyIndexCommand());
        return TemplateNames.EMERGENCY_INDEX_TEMPLATE;
    }

    @RequestMapping("/emergency/close-connection")
    public String closeConnection() {
        emergencyIndexService.closeConnection();
        return "redirect:/emergency";
    }

    @PostMapping
    @RequestMapping("/emergency/open-connection")
    public String openConnection(@Valid @ModelAttribute EmergencyIndexCommand emergencyIndexCommand, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (emergencyIndexCommand.getConnectionState() == null) {
                emergencyIndexCommand.setConnectionState(CloudConnectionState.OFF);
            }
            model.addAttribute("emergencyIndexCommand", emergencyIndexCommand);
            return TemplateNames.EMERGENCY_INDEX_TEMPLATE;
        }
        this.emergencyIndexService.openConnection(emergencyIndexCommand.getQrCode());
        return "redirect:/emergency";
    }
}

