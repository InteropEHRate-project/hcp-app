package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPrescription;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandVitalSigns;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.EHRType;
import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.EHRModel;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.TransferredPatientModel;
import eu.interopehrate.hcpapp.services.currentpatient.SendToOtherHcpService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SendToOtherHcpServiceImpl implements SendToOtherHcpService {
    private final CurrentPatient currentPatient;
    private final IndexService indexService;
    private final RestTemplate restTemplate;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final EntityToCommandPrescription entityToCommandPrescription;
    private final EntityToCommandVitalSigns entityToCommandVitalSigns;
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public SendToOtherHcpServiceImpl(CurrentPatient currentPatient, IndexService indexService, RestTemplate restTemplate,
                                     PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository,
                                     EntityToCommandPrescription entityToCommandPrescription, EntityToCommandVitalSigns entityToCommandVitalSigns, HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.currentPatient = currentPatient;
        this.indexService = indexService;
        this.restTemplate = restTemplate;
        this.prescriptionRepository = prescriptionRepository;
        this.vitalSignsRepository = vitalSignsRepository;
        this.entityToCommandPrescription = entityToCommandPrescription;
        this.entityToCommandVitalSigns = entityToCommandVitalSigns;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @Getter
    @Setter
    @Builder
    private static class PatientHcp {
        String patientId;
        Long hcpId;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List hcpsList() {
        try {
            return this.restTemplate.getForObject(this.hospitalServicesUrl + "/hcps" + "/list", List.class);
        } catch (ResourceAccessException e) {
            log.error("Connection refused");
            return null;
        }
    }

    @Override
    public Boolean sendPatient(Long hcpId) {
        String patientId = this.currentPatient.getPatient().getId();
        if (!this.restTemplate.postForObject(this.hospitalServicesUrl + "/patients" + "/is-patient-existing-already", patientId, Boolean.class)) {
            this.sendCurrentPatient(hcpId);
            this.sendEHRs();
            this.sendPrescription();
            this.sendVitalSigns();
            this.currentPatient.reset();
            return Boolean.TRUE;
        }
        if (this.restTemplate.postForObject(this.hospitalServicesUrl + "/patients" + "/send-patient-to-hcp", PatientHcp.builder().patientId(patientId).hcpId(hcpId).build(), Boolean.class)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @SneakyThrows
    public void sendCurrentPatient(Long hcpId) {
        TransferredPatientModel transferredPatientModel = new TransferredPatientModel();
        transferredPatientModel.setInitialHcpId(this.healthCareProfessionalRepository.findAll().get(0).getId());
        transferredPatientModel.setHcpId(hcpId);
        if (Objects.nonNull(this.currentPatient.getPatient()) &&
                Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand()) &&
                Objects.nonNull(this.indexService.indexCommand().getPatientDataCommand().getId())) {

            IndexPatientDataCommand patientDataCommand = this.indexService.indexCommand().getPatientDataCommand();
            transferredPatientModel.setPatientId(patientDataCommand.getId());
            if (Objects.nonNull(patientDataCommand.getFirstName()) && Objects.nonNull(patientDataCommand.getLastName())) {
                transferredPatientModel.setName(patientDataCommand.getFirstName() + " " + patientDataCommand.getLastName());
            }
            if (Objects.nonNull(patientDataCommand.getAge())) {
                transferredPatientModel.setAge(patientDataCommand.getAge());
            }
            if (Objects.nonNull(patientDataCommand.getCountry())) {
                transferredPatientModel.setCountry(patientDataCommand.getCountry());
            }
            this.restTemplate.postForLocation(this.hospitalServicesUrl + "/patients" + "/transfer", transferredPatientModel);
        }
    }

    @SneakyThrows
    public void sendEHRs() {
        String patientId = this.currentPatient.getPatient().getId();
        if (Objects.nonNull(this.currentPatient.getPatient())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PATIENT_DEMOGRAPHIC_DATA);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertPatientIntoString(this.currentPatient.getPatient()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }

        if (Objects.nonNull(this.currentPatient.getPatientSummaryBundle())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.IPS);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getPatientSummaryBundle()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(this.currentPatient.getPrescription())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PRESCRIPTION);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getPrescription()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(this.currentPatient.getLaboratoryResults())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.LAB_RESULTS);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getLaboratoryResults()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(this.currentPatient.getVitalSigns())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.VITAL_SIGNS);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getVitalSigns()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(this.currentPatient.getImageReport())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.IMAGE_REPORT);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getImageReport()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(this.currentPatient.getPatHisBundle()))  {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PATHOLOGY_HISTORY);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getPatHisBundle()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(this.currentPatient.getDocHistoryConsult())) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.DOC_HISTORY);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(this.currentPatient.getDocHistoryConsult()));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel, Boolean.class);
        }
    }

    @Override
    public void sendPrescription() {
        for (PrescriptionEntity pr : this.prescriptionRepository.findAll()) {
            this.restTemplate.postForLocation(this.hospitalServicesUrl + "/ehrs" + "/transfer-prescription", this.entityToCommandPrescription.convert(pr));
        }
        this.prescriptionRepository.deleteAll();
    }

    @Override
    public void sendVitalSigns() {
        for (VitalSignsEntity v : this.vitalSignsRepository.findAll()) {
            this.restTemplate.postForLocation(this.hospitalServicesUrl + "/ehrs" + "/transfer-vital-signs", this.entityToCommandVitalSigns.convert(v));
        }
        this.vitalSignsRepository.deleteAll();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public String getTransferHcpName(Long hcpId) {
        //For displaying the Hcp name where the patient was sent
        try {
            for (var hcp : this.hcpsList()) {
                if (hcp instanceof LinkedHashMap && ((LinkedHashMap) hcp).get("id").equals(hcpId.intValue())) {
                    return ((LinkedHashMap) hcp).get("name").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static String convertBundleIntoString(Bundle bundle) throws IOException {
        IParser iParser = new JsonParser();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        iParser.compose(byteArrayOutputStream, bundle);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toString();
    }

    private static String convertPatientIntoString(Patient patient) throws IOException {
        IParser iParser = new JsonParser();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        iParser.compose(byteArrayOutputStream, patient);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toString();
    }
}
