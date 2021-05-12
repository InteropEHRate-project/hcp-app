package eu.interopehrate.hcpapp.services.index.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.linkedhashmap.LinkedHashMapToPrescriptionEntity;
import eu.interopehrate.hcpapp.converters.linkedhashmap.LinkedHashMapToVitalSignsEntity;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import eu.interopehrate.hcpapp.services.index.ContinueExistingVisitService;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@Service
public class ContinueExistingVisitServiceImpl implements ContinueExistingVisitService {
    private final RestTemplate restTemplate;
    private final CurrentPatient currentPatient;
    private final TranslateService translateService;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final LinkedHashMapToPrescriptionEntity linkedHashMapToPrescriptionEntity;
    private final LinkedHashMapToVitalSignsEntity linkedHashMapToVitalSignsEntity;
    @Value("${hcp.app.hospital.services.url}")
    private String url;
    public static Boolean isExtractedData = false;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public ContinueExistingVisitServiceImpl(RestTemplate restTemplate, CurrentPatient currentPatient, TranslateService translateService,
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
        isExtractedData = true;
        List ehrs = this.restTemplate.postForObject(this.url + "/ehrs" + "/list", patientId, List.class);

        IParser parser = FhirContext.forR4().newJsonParser();
        for (Object ehr : ehrs) {
            String str = (String) ((LinkedHashMap) ehr).get("CONTENT");
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("PATIENT_DEMOGRAPHIC_DATA")) {
                this.currentPatient.setPatient(parser.parseResource(Patient.class, str));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("IPS")) {
                this.currentPatient.setPatientSummaryBundle(parser.parseResource(Bundle.class, str));
                this.currentPatient.setPatientSummaryBundleTranslated(this.translateService.translate(this.currentPatient.getPatientSummaryBundle(), Locale.UK));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("PRESCRIPTION")) {
                this.currentPatient.setPrescription(parser.parseResource(Bundle.class, str));
                this.currentPatient.setPrescriptionTranslated(this.translateService.translate(this.currentPatient.getPrescription(), Locale.UK));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("LAB_RESULTS")) {
                this.currentPatient.setLaboratoryResults(parser.parseResource(Bundle.class, str));
                this.currentPatient.setLaboratoryResultsTranslated(this.translateService.translate(this.currentPatient.getLaboratoryResults(), Locale.UK));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("IMAGE_REPORT")) {
                this.currentPatient.setImageReport(parser.parseResource(Bundle.class, str));
                this.currentPatient.setImageReportTranslated(this.translateService.translate(this.currentPatient.getImageReport(), Locale.UK));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("DOC_HISTORY")) {
                this.currentPatient.setDocHistoryConsult(parser.parseResource(Bundle.class, str));
                this.currentPatient.setDocHistoryConsultTranslated(this.translateService.translate(this.currentPatient.getDocHistoryConsult(), Locale.UK));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("PATHOLOGY_HISTORY")) {
                this.currentPatient.setPatHisBundle((parser.parseResource(Bundle.class, str)));
                this.currentPatient.setPatHisBundleTranslated(this.translateService.translate(this.currentPatient.getPatHisBundle(), Locale.UK));
                continue;
            }
            if (((LinkedHashMap) ehr).get("EHR_TYPE").equals("VITAL_SIGNS")) {
                this.currentPatient.setVitalSignsBundle(parser.parseResource(Bundle.class, str));
                this.currentPatient.setVitalSignsTranslated(this.translateService.translate(this.currentPatient.getVitalSigns(), Locale.UK));
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
    public void clearData() {
        ContinueExistingVisitServiceImpl.isExtractedData = false;
        this.currentPatient.reset();
        this.prescriptionRepository.deleteAll();
        this.vitalSignsRepository.deleteAll();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getListOfPatients() {
        return restTemplate.postForObject(this.url + "/patients" + "/list", this.healthCareProfessionalRepository.findAll().get(0).getId(), List.class);
    }
}
