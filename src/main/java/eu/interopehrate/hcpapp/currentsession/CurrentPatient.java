package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.ihs.terminalclient.services.CodesConversionService;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.cert.Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
public class CurrentPatient {
    private static final Logger logger = LoggerFactory.getLogger(CurrentPatient.class);
    private final TranslateService translateService;
    private final CodesConversionService codesConversionService;
    private Boolean displayTranslatedVersion = Boolean.TRUE;
    private Patient patient;
    private Consent consent;
    private Bundle patientSummaryBundle;
    private Bundle patientSummaryTranslatedBundle;
    private Certificate certificate;
    private Bundle prescription;
    private List<Observation> observation;

    public Bundle getPrescription() {
        return prescription;
    }

    public void setPrescription(Bundle prescription) {
        this.prescription = prescription;
    }

    public CurrentPatient(TranslateService translateService, CodesConversionService codesConversionService) {
        this.translateService = translateService;
        this.codesConversionService = codesConversionService;
    }

    public TranslateService getTranslateService() {
        return translateService;
    }

    public void initPatient(Patient patient) {
        this.patient = patient;
    }

    public void initConsent(String consent) {
        String consentJson = consent.substring(consent.indexOf("{") + 1);
        consentJson = consentJson.substring(0, consentJson.lastIndexOf("}"));
        this.consent = (Consent) FhirContext.forR4().newJsonParser().parseResource(consentJson);
    }

    public void initPatientSummary(Bundle patientSummary) {
        try {
            patientSummaryTranslatedBundle = translateService.translate(patientSummary, Locale.ITALY, Locale.UK);
            patientSummaryBundle = codesConversionService.convert(patientSummaryTranslatedBundle);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            patientSummaryBundle = patientSummary;
        }
    }

    public void initPrescription(Bundle prescription) {
        this.prescription = prescription;
    }

    public void initLaboratoryResults(List<Observation> obs) {
        this.observation = obs;
    }

    public List<Observation> getObservation(){
        if (Objects.isNull(observation)) {
            return Collections.emptyList();
        }else{
            return observation;
        }
    }

    public void reset() {
        displayTranslatedVersion = Boolean.TRUE;
        patient = null;
        consent = null;
        patientSummaryBundle = null;
        this.prescription = null;
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        if (Objects.isNull(patientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle).allergyIntoleranceList();
        }
    }

    public List<Observation> observationList() {
        if (Objects.isNull(patientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle).observationList();
        }
    }

    public List<Condition> conditionsList() {
        if (Objects.isNull(patientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle).conditionList();
        }
    }


    public List<MedicationStatement> medicationStatementList() {
        if (Objects.isNull(patientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle).medicationStatementList();
        }
    }

    public List<MedicationRequest> prescriptionList() {
        if (Objects.isNull(this.prescription)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(this.prescription).prescriptionList();
        }
    }

    public Boolean getDisplayTranslatedVersion() {
        return this.displayTranslatedVersion;
    }

    public void setDisplayTranslatedVersion(Boolean displayTranslatedVersion) {
        this.displayTranslatedVersion = displayTranslatedVersion;
    }

    public Patient getPatient() {
        return patient;
    }

    public Consent getConsent() {
        return consent;
    }

    public String getConsentAsString() {
        return consent.getText().getDiv().toString().replaceAll("[<](/)?div[^>]*[>]", "");
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public String getCertificateAsString() {
        return certificate.toString();
    }

    // todo - nicuj, code review
    public static String extractExtensionText(Coding coding, CurrentPatient currentPatient) {
        if (currentPatient.getDisplayTranslatedVersion() && coding.hasExtension()) {
            return coding.getExtension().get(0).getExtension().get(1).getValue().toString();
        } else {
            return coding.getDisplay();
        }
    }
}