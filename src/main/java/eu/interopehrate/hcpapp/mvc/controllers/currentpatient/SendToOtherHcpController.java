package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
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

import java.util.LinkedHashMap;
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
    @Value("${hcp.app.hospital.services.patients.transfer.url}")
    private String patientsTransferUrl;
    private final CurrentPatient currentPatient;
    private final RestTemplate restTemplate;
    private final IndexService indexService;
    private List hcpList;

    public SendToOtherHcpController(CurrentPatient currentPatient, RestTemplate restTemplate, IndexService indexService) {
        this.currentPatient = currentPatient;
        this.restTemplate = restTemplate;
        this.indexService = indexService;
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
        boolean isWorking = false;
        TransferredPatientModel transferredPatientModel = new TransferredPatientModel();
        transferredPatientModel.setInitialHcpId(initialHcpId);
        if (Objects.nonNull(this.currentPatient.getPatient()) && Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getId())) {
            transferredPatientModel.setPatientId(Long.valueOf(this.indexService.indexCommand().getPatientDataCommand().getId()));
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getFirstName()) &&
                    Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getLastName())) {
                transferredPatientModel.setName(this.indexService.indexCommand().getPatientDataCommand().getFirstName()
                        + " " + this.indexService.indexCommand().getPatientDataCommand().getLastName());
            }
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getAge())) {
                transferredPatientModel.setAge(this.indexService.indexCommand().getPatientDataCommand().getAge());
            }
            if (Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getCountry())) {
                transferredPatientModel.setCountry(this.indexService.indexCommand().getPatientDataCommand().getCountry());
            }
            this.restTemplate.postForLocation(this.hospitalServicesUrl + this.patientsTransferUrl, transferredPatientModel);
            isWorking = true;
            model.addAttribute("isWorking", isWorking);
            return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
        }

//        for testing purposes if the connection with S-EHR is not possible
//        transferredPatientModel.setPatientId(2021L);
//        transferredPatientModel.setName("Sece");
//        transferredPatientModel.setAge(28);
//        transferredPatientModel.setCountry("RO");
//        this.restTemplate.postForLocation(this.hospitalServicesUrl + this.patientsTransferUrl, transferredPatientModel);
//        isWorking = true;

        model.addAttribute("isWorking", isWorking);
        for (var hcp : this.hcpList) {
            if (hcp instanceof LinkedHashMap && ((LinkedHashMap) hcp).get("id").equals(transferredPatientModel.getInitialHcpId().intValue())) {
                model.addAttribute("hcpName", ((LinkedHashMap) hcp).get("name"));
            }
        }
        return TemplateNames.CURRENT_PATIENT_SEND_TO_OTHER_HCP;
    }
}
