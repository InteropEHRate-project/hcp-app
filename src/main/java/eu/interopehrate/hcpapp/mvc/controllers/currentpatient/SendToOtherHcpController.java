package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/current-patient/send-to-other-hcp")
public class SendToOtherHcpController {
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    @Value("${hcp.app.hospital.services.hcps.list.url}")
    private String hcpsListUrl;

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        boolean error = false;
        try {
            model.addAttribute("hcps", new RestTemplate().getForObject(this.hospitalServicesUrl + this.hcpsListUrl, List.class));
        } catch (ResourceAccessException e) {
            log.error("Connection refused");
            error = true;
        } finally {
            model.addAttribute("error", error);
        }
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }
}
