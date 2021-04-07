package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.enums.EHRType;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.EHRModel;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.TransferredPatientModel;
import eu.interopehrate.hcpapp.services.currentpatient.PermanentStorageOfCloudDataService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class PermanentStorageOfCloudDataServiceImpl implements PermanentStorageOfCloudDataService {
    private final RestTemplate restTemplate;
    private final CurrentPatient currentPatient;
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;
    private final IndexService indexService;

    public PermanentStorageOfCloudDataServiceImpl(RestTemplate restTemplate, CurrentPatient currentPatient,
                                                  HealthCareProfessionalRepository healthCareProfessionalRepository, IndexService indexService) {
        this.restTemplate = restTemplate;
        this.currentPatient = currentPatient;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
        this.indexService = indexService;
    }

    @Override
    @SneakyThrows
    public void storePatientData() {
        Patient patient = this.currentPatient.getPatient();
        Bundle ips = this.currentPatient.getPatientSummaryBundle();
        Bundle prescriptions = this.currentPatient.getPrescription();
        Bundle laboratoryResults = this.currentPatient.getLaboratoryResults();
        String patientId = this.currentPatient.getPatient().getId();
        IndexPatientDataCommand patientDataCommand = this.indexService.indexCommand().getPatientDataCommand();
        if (Objects.nonNull(patient) &&
                Objects.nonNull(patientDataCommand) &&
                Objects.nonNull(patientDataCommand.getId())) {
            TransferredPatientModel transferredPatientModel = new TransferredPatientModel();
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
            transferredPatientModel.setInitialHcpId(this.healthCareProfessionalRepository.findAll().get(0).getId());
            this.restTemplate.postForLocation(this.hospitalServicesUrl + "/patients" + "/permanent-storage", transferredPatientModel);
        }
        if (Objects.nonNull(patient)) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PATIENT_DEMOGRAPHIC_DATA);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertPatientIntoString(patient));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/permanent-storage", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(ips)) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.IPS);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(ips));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/permanent-storage", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(prescriptions)) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.PRESCRIPTION);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(prescriptions));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/permanent-storage", ehrModel, Boolean.class);
        }
        if (Objects.nonNull(laboratoryResults)) {
            EHRModel ehrModel = new EHRModel();
            ehrModel.setEhrType(EHRType.LAB_RESULTS);
            ehrModel.setPatientId(patientId);
            ehrModel.setContent(convertBundleIntoString(laboratoryResults));
            this.restTemplate.postForObject(this.hospitalServicesUrl + "/ehrs" + "/permanent-storage", ehrModel, Boolean.class);
        }
    }

    private static String convertBundleIntoString(Bundle bundle) throws IOException {
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
