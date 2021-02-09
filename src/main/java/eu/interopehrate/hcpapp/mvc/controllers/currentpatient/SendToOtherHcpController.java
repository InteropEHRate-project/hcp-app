package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.SendToOtherHcpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/current-patient/send-to-other-hcp")
public class SendToOtherHcpController {
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    @Value("${hcp.app.hospital.services.hcps.list.url}")
    private String hcpsListUrl;
    @Value("${hcp.app.hospital.services.patients.transfer.url}")
    private String patientsTransferUrl;
    private final CurrentPatient currentPatient;
    private final RestTemplate restTemplate;
    private List hcpList;
    private final SendToOtherHcpService sendToOtherHcpService;

    public SendToOtherHcpController(CurrentPatient currentPatient, RestTemplate restTemplate, SendToOtherHcpService sendToOtherHcpService) {
        this.currentPatient = currentPatient;
        this.restTemplate = restTemplate;
        this.sendToOtherHcpService = sendToOtherHcpService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("patient", this.currentPatient.getPatient());
        boolean error = false;
        try {
            this.hcpList = this.restTemplate.getForObject(this.hospitalServicesUrl + this.hcpsListUrl, List.class);
            model.addAttribute("hcps", this.hcpList);
        } catch (ResourceAccessException e) {
            log.error("Connection refused");
            error = true;
        } finally {
            model.addAttribute("error", error);
        }
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }

    @GetMapping
    @RequestMapping("/send-patient")
    public String sendPatient(@RequestParam(name = "initialHcpId") Long initialHcpId, Model model) throws Exception {
        model.addAttribute("isWorking", this.sendToOtherHcpService.sendCurrentPatient(initialHcpId));

        //For displaying the Hcp name where the patient was sent
        for (var hcp : this.hcpList) {
            if (hcp instanceof LinkedHashMap && ((LinkedHashMap) hcp).get("id").equals(initialHcpId.intValue())) {
                model.addAttribute("hcpName", ((LinkedHashMap) hcp).get("name"));
            }
        }
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }
}
