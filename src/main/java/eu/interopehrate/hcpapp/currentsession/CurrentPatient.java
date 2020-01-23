package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.hl7.fhir.r4.model.*;
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
    private Consent consent;
    private Bundle initialPatientSummaryBundle;
    private Bundle translatedPatientSummaryBundle;

    public CurrentPatient(TranslateService translateService) {
        this.translateService = translateService;
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

    public Consent getConsent() {
        return consent;
    }

    public String getConsentAsString() {
        return consent.getText().getDiv().toString().replaceAll("[<](/)?div[^>]*[>]", "");
    }

    private Bundle patientSummaryBundle() {
        return displayTranslatedVersion ? translatedPatientSummaryBundle : initialPatientSummaryBundle;
    }
}