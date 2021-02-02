package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/administration/hcps")
public class HcpsController {
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    @Value("${hcp.app.hospital.services.hcps.list.url}")
    private String hcpsListUrl;

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsTemplate(Model model) {
        model.addAttribute("hcps", new RestTemplate().getForObject(this.hospitalServicesUrl + this.hcpsListUrl, List.class));
        return TemplateNames.ADMINISTRATION_HCPS_VIEW_DETAILS;
    }
}
