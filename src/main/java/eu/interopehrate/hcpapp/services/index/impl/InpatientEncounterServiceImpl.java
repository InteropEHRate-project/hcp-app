package eu.interopehrate.hcpapp.services.index.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.linkedhashmap.LinkedHashMapToPrescriptionEntity;
import eu.interopehrate.hcpapp.converters.linkedhashmap.LinkedHashMapToVitalSignsEntity;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.services.index.InpatientEncounterService;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class InpatientEncounterServiceImpl implements InpatientEncounterService {
    private final RestTemplate restTemplate;
    private final CurrentPatient currentPatient;
    private final TranslateService translateService;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final LinkedHashMapToPrescriptionEntity linkedHashMapToPrescriptionEntity;
    private final LinkedHashMapToVitalSignsEntity linkedHashMapToVitalSignsEntity;
    @Value("${hcp.app.hospital.services.url}")
    private String url;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public InpatientEncounterServiceImpl(RestTemplate restTemplate, CurrentPatient currentPatient, TranslateService translateService,
                                         PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository,
                                         LinkedHashMapToPrescriptionEntity linkedHashMapToPrescriptionEntity,
                                         LinkedHashMapToVitalSignsEntity linkedHashMapToVitalSignsEntity, HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.restTemplate = restTemplate;
        this.currentPatient = currentPatient;
        this.translateService = translateService;
        this.prescriptionRepository = prescriptionRepository;
        this.vitalSignsRepository = vitalSignsRepository;
        this.linkedHashMapToPrescriptionEntity = linkedHashMapToPrescriptionEntity;
        this.linkedHashMapToVitalSignsEntity = linkedHashMapToVitalSignsEntity;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void retrieveEHRs(String patientId) {
        ContinueExistingVisitServiceImpl.isExtractedData = true;
        List ehrs = this.restTemplate.postForObject(this.url + "/ehrs" + "/list", patientId, List.class);

        IParser parser = FhirContext.forR4().newJsonParser();
        for (Object ehr : ehrs) {
            String str = (String) ((LinkedHashMap) ehr).get("CONTENT");
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("PATIENT_DEMOGRAPHIC_DATA")) {
                this.currentPatient.initPatient(parser.parseResource(Patient.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("IPS")) {
                this.currentPatient.initPatientSummary(parser.parseResource(Bundle.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("PRESCRIPTION")) {
                this.currentPatient.initPrescription(parser.parseResource(Bundle.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("LAB_RESULTS")) {
                this.currentPatient.initLaboratoryResults(parser.parseResource(Bundle.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("IMAGE_REPORT")) {
                this.currentPatient.initImageReport(parser.parseResource(Bundle.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("DOC_HISTORY")) {
                this.currentPatient.initDocHistoryConsultation(parser.parseResource(Bundle.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("PATHOLOGY_HISTORY")) {
                this.currentPatient.initPatHisConsultation(parser.parseResource(Bundle.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("VITAL_SIGNS")) {
                this.currentPatient.initVitalSigns(parser.parseResource(Bundle.class, str));
            }
        }

        var prescriptions = this.restTemplate.postForObject(this.url + "/ehrs" + "/retrieve-added-prescription", patientId , List.class);
        for (Object prescription : prescriptions) {
            this.prescriptionRepository.save(this.linkedHashMapToPrescriptionEntity.convert((LinkedHashMap) prescription));
        }
        var vitalSigns = this.restTemplate.postForObject(this.url + "/ehrs" + "/retrieve-added-vital-signs", patientId, List.class);
        for (Object vitalSign : vitalSigns) {
            this.vitalSignsRepository.save(this.linkedHashMapToVitalSignsEntity.convert((LinkedHashMap) vitalSign));
        }
        IndexCommand.transmissionCompleted = Boolean.TRUE;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getListOfPatients() {
        return restTemplate.postForObject(this.url + "/patients" + "/list", this.healthCareProfessionalRepository.findAll().get(0).getId(), List.class);
    }
}
