package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.TransferredPatientModel;
import eu.interopehrate.hcpapp.services.index.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/current-patient/send-to-other-hcp")
public class SendToOtherHcpController {
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    @Value("${hcp.app.hospital.services.hcps.list.url}")
    private String hcpsListUrl;
    private final CurrentD2DConnection currentD2DConnection;
    private final RestTemplate restTemplate;
    private final IndexService indexService;

    public SendToOtherHcpController(CurrentD2DConnection currentD2DConnection, RestTemplate restTemplate, IndexService indexService) {
        this.currentD2DConnection = currentD2DConnection;
        this.restTemplate = restTemplate;
        this.indexService = indexService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("connectedThread", this.currentD2DConnection.getConnectedThread());
        boolean error = false;
        try {
            model.addAttribute("hcps", this.restTemplate.getForObject(this.hospitalServicesUrl + this.hcpsListUrl, List.class));
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
    public String sendPatient(@RequestParam(name = "initialHcpId") Long initialHcpId) throws Exception {
        TransferredPatientModel transferredPatientModel = new TransferredPatientModel();
        transferredPatientModel.setInitialHcpId(initialHcpId);
        if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand())) {
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getId())) {
                transferredPatientModel.setPatientId(Long.valueOf(this.indexService.indexCommand().getPatientDataCommand().getId()));
            }
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getFirstName()) && Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getLastName())) {
                transferredPatientModel.setName(this.indexService.indexCommand().getPatientDataCommand().getFirstName() + " " + this.indexService.indexCommand().getPatientDataCommand().getLastName());
            }
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getAge())) {
                transferredPatientModel.setAge(this.indexService.indexCommand().getPatientDataCommand().getAge());
            }
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getCountry())) {
                transferredPatientModel.setCountry(this.indexService.indexCommand().getPatientDataCommand().getCountry());
            }
        }
        this.restTemplate.postForLocation("", transferredPatientModel);
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }
}
