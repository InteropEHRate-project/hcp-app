package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.TransferredPatientModel;
import eu.interopehrate.hcpapp.services.currentpatient.SendToOtherHcpService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class SendToOtherHcpServiceImpl implements SendToOtherHcpService {
    private final CurrentPatient currentPatient;
    private final IndexService indexService;
    private final RestTemplate restTemplate;
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    @Value("${hcp.app.hospital.services.hcps.list.url}")
    private String hcpsListUrl;
    @Value("${hcp.app.hospital.services.patients.transfer.url}")
    private String patientsTransferUrl;

    public SendToOtherHcpServiceImpl(CurrentPatient currentPatient, IndexService indexService, RestTemplate restTemplate) {
        this.currentPatient = currentPatient;
        this.indexService = indexService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean sendCurrentPatient(Long initialHcpId) throws Exception {
        TransferredPatientModel transferredPatientModel = new TransferredPatientModel();
        transferredPatientModel.setInitialHcpId(initialHcpId);
        if (Objects.nonNull(this.currentPatient.getPatient()) &&
                Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand()) &&
                Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getId())) {

            IndexPatientDataCommand patientDataCommand = this.indexService.indexCommand().getPatientDataCommand();
            transferredPatientModel.setPatientId(Long.valueOf(patientDataCommand.getId()));
            if (Objects.nonNull(patientDataCommand.getFirstName()) && Objects.nonNull(patientDataCommand.getLastName())) {
                transferredPatientModel.setName(patientDataCommand.getFirstName() + " " + patientDataCommand.getLastName());
            }
            if (Objects.nonNull(patientDataCommand.getAge())) {
                transferredPatientModel.setAge(patientDataCommand.getAge());
            }
            if (Objects.nonNull(patientDataCommand.getCountry())) {
                transferredPatientModel.setCountry(patientDataCommand.getCountry());
            }
            this.restTemplate.postForLocation(this.hospitalServicesUrl + this.patientsTransferUrl, transferredPatientModel);
            return true;
        }
        //        for testing purposes if the connection with S-EHR is not possible
//        transferredPatientModel.setPatientId(2021L);
//        transferredPatientModel.setName("Sece");
//        transferredPatientModel.setAge(28);
//        transferredPatientModel.setCountry("RO");
//        this.restTemplate.postForLocation(this.hospitalServicesUrl + this.patientsTransferUrl, transferredPatientModel);
//        return true;
        return false;
    }

    @Override
    public void sendEHRs() {

    }
}
