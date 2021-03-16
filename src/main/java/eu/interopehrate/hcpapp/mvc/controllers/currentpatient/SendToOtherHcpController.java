package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.SendToOtherHcpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/current-patient/send-to-other-hcp")
public class SendToOtherHcpController {
    private final CurrentPatient currentPatient;
    private final SendToOtherHcpService sendToOtherHcpService;

    public SendToOtherHcpController(CurrentPatient currentPatient, SendToOtherHcpService sendToOtherHcpService) {
        this.currentPatient = currentPatient;
        this.sendToOtherHcpService = sendToOtherHcpService;
    }

    @GetMapping
    @SuppressWarnings("rawtypes")
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patient", this.currentPatient.getPatient());
        List hcpList = this.sendToOtherHcpService.hcpsList();
        boolean error;
        if (Objects.isNull(hcpList)) {
            error = true;
            hcpList = Collections.emptyList();
        } else {
            error = false;
        }
        model.addAttribute("error", error);
        model.addAttribute("hcps", hcpList);
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }

    @GetMapping
    @RequestMapping("/send-patient")
    public String sendPatient(@RequestParam(name = "hcpId") Long hcpId, HttpSession session) {
        if (this.sendToOtherHcpService.sendPatient(hcpId)) {
            session.setAttribute("isWorking", true);
            session.setAttribute("transferHcpName", this.sendToOtherHcpService.getTransferHcpName(hcpId));
            if (Objects.nonNull(session.getAttribute("patientNavbar"))) {
                session.removeAttribute("patientNavbar");
            }
            session.setAttribute("alreadyAdded", Boolean.FALSE);
            return "redirect:/index/close-connection";
        }
        session.setAttribute("transferHcpName", this.sendToOtherHcpService.getTransferHcpName(hcpId));
        session.setAttribute("alreadyAdded", Boolean.TRUE);
        return "redirect:/current-patient/send-to-other-hcp/view-section";
    }
}
