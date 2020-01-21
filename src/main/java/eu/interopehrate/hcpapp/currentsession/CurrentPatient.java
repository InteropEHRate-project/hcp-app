package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
public class CurrentPatient {
    private static final Logger logger = LoggerFactory.getLogger(CurrentPatient.class);
    private final TranslateService translateService;
    private Boolean displayTranslatedVersion = Boolean.TRUE;
    private Patient patient;
    private String consent;
    private Bundle initialPatientSummaryBundle;
    private Bundle translatedPatientSummaryBundle;

    public CurrentPatient(TranslateService translateService) {
        this.translateService = translateService;
    }

    public void initPatient(Patient patient) {
        this.patient = patient;
    }

    public void initConsent(String consent) {
        this.consent = consent;
    }

    public void initPatientSummary(Bundle patientSummary) {
        initialPatientSummaryBundle = patientSummary;
        try {
            translatedPatientSummaryBundle = translateService.translate(initialPatientSummaryBundle, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            translatedPatientSummaryBundle = initialPatientSummaryBundle;
        }
    }

    public void reset() {
        displayTranslatedVersion = Boolean.TRUE;
        patient = null;
        consent = null;
        initialPatientSummaryBundle = null;
        translatedPatientSummaryBundle = null;
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        if (Objects.isNull(patientSummaryBundle())) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle()).allergyIntoleranceList();
        }
    }

    public List<Observation> observationList() {
        if (Objects.isNull(patientSummaryBundle())) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle()).observationList();
        }
    }

    public void setDisplayTranslatedVersion(Boolean displayTranslatedVersion) {
        this.displayTranslatedVersion = displayTranslatedVersion;
    }

    public Boolean getDisplayTranslatedVersion() {
        return this.displayTranslatedVersion;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getConsent() {
        return consent;
    }

    private Bundle patientSummaryBundle() {
        return displayTranslatedVersion ? translatedPatientSummaryBundle : initialPatientSummaryBundle;
    }
}