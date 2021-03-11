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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/current-patient/send-to-other-hcp")
public class SendToOtherHcpController {
    private final CurrentPatient currentPatient;
    @SuppressWarnings("rawtypes")
    private List hcpList;
    private final SendToOtherHcpService sendToOtherHcpService;

    public SendToOtherHcpController(CurrentPatient currentPatient, SendToOtherHcpService sendToOtherHcpService) {
        this.currentPatient = currentPatient;
        this.sendToOtherHcpService = sendToOtherHcpService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patient", this.currentPatient.getPatient());
        this.hcpList = this.sendToOtherHcpService.hcpsList();
        boolean error;
        if (Objects.isNull(this.hcpList)) {
            error = true;
            this.hcpList = Collections.emptyList();
        } else {
            error = false;
        }
        model.addAttribute("error", error);
        model.addAttribute("hcps", this.hcpList);
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }

    @SuppressWarnings("rawtypes")
    @GetMapping
    @RequestMapping("/send-patient")
    public String sendPatient(@RequestParam(name = "hcpId") Long hcpId, HttpSession session) throws Exception {
        session.setAttribute("isWorking", this.sendToOtherHcpService.sendCurrentPatient(hcpId));
        session.setAttribute("isEhrTransferred", this.sendToOtherHcpService.sendEHRs());

        //For displaying the Hcp name where the patient was sent
        try {
            for (var hcp : this.hcpList) {
                if (hcp instanceof LinkedHashMap && ((LinkedHashMap) hcp).get("id").equals(hcpId.intValue())) {
                    session.setAttribute("transferHcpName", ((LinkedHashMap) hcp).get("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("transferHcpName", "");
        }

        this.sendToOtherHcpService.sendPrescription();
        this.sendToOtherHcpService.sendVitalSigns();
        this.currentPatient.reset();
        if (Objects.nonNull(session.getAttribute("mySessionAttribute"))) {
            session.removeAttribute("mySessionAttribute");
        }
        return "redirect:/index/close-connection";
    }
}
