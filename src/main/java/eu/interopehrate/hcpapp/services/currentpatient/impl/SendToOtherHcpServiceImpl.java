package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.enums.EHRType;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.EHRModel;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.TransferredPatientModel;
import eu.interopehrate.hcpapp.services.currentpatient.SendToOtherHcpService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
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
    public List hcpsList() {
        try {
            return this.restTemplate.getForObject(this.hospitalServicesUrl + this.hcpsListUrl, List.class);
        } catch (ResourceAccessException e) {
            log.error("Connection refused");
            return null;
        }
    }

    @Override
    public Boolean sendCurrentPatient(Long initialHcpId) throws Exception {
        TransferredPatientModel transferredPatientModel = new TransferredPatientModel();
        transferredPatientModel.setInitialHcpId(initialHcpId);
        if (Objects.nonNull(this.currentPatient.getPatient()) &&
                Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand()) &&
                Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getId())) {

            IndexPatientDataCommand patientDataCommand = this.indexService.indexCommand().getPatientDataCommand();
            transferredPatientModel.setPatientId(Long.valueOf(patientDataCommand.getId().substring(patientDataCommand.getId().indexOf("/") + 1)));
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
    public Boolean sendEHRs() throws IOException {
        boolean isAnyEHRTransferred = false;
        if (Objects.nonNull(this.currentPatient.getPatientSummaryBundle())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.IPS);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getPatientSummaryBundle()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }
        if (Objects.nonNull(this.currentPatient.getPrescription())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PRESCRIPTION);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getPrescription()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }
        if (Objects.nonNull(this.currentPatient.getLaboratoryResults())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.LAB_RESULTS);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getLaboratoryResults()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }
        if (Objects.nonNull(this.currentPatient.getVitalSigns())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.VITAL_SIGNS);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getVitalSigns()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }
        if (Objects.nonNull(this.currentPatient.getImageReport())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.IMAGE_REPORT);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getImageReport()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }
        if (Objects.nonNull(this.currentPatient.getPatHisBundle()))  {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PATHOLOGY_HISTORY);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getPatHisBundle()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }
        if (Objects.nonNull(this.currentPatient.getDocHistoryConsult())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.DOC_HISTORY);
            ehrModel.setPatientId(Long.valueOf(this.currentPatient.getPatient().getId().substring(this.currentPatient.getPatient().getId().indexOf("/") + 1)));
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getDocHistoryConsult()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
            isAnyEHRTransferred = true;
        }

        //        for testing purposes if the connection with S-EHR is not possible
//        EHRModel ehrModel = new EHRModel();
//        ehrModel.setPatientId(1L);
//        ehrModel.setEhrType(EHRType.IPS);
//        ehrModel.setContent(convertBundleIntoString(new Bundle()));
//        return this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);

        return isAnyEHRTransferred;
    }

    private static String convertBundleIntoString(Bundle bundle) throws IOException {
        IParser iParser = new JsonParser();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        iParser.compose(byteArrayOutputStream, bundle);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toString();
    }
}
