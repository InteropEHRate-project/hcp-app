package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
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
    private Bundle initialPatientSummaryBundle;
    private Bundle translatedPatientSummaryBundle;
    private Patient patient;

    public CurrentPatient(TranslateService translateService) {
        this.translateService = translateService;
    }

    public void intiFromJsonString(String patientSummaryJson) {
        initialPatientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummaryJson);
        try {
            translatedPatientSummaryBundle = translateService.translate(initialPatientSummaryBundle, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            translatedPatientSummaryBundle = initialPatientSummaryBundle;
        }
    }

    public void initPatient(Patient patient) {
        this.patient = patient;
    }

    public void reset() {
        translatedPatientSummaryBundle = null;
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        if (Objects.isNull(translatedPatientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(translatedPatientSummaryBundle).allergyIntoleranceList();
        }
    }

    public List<Observation> observationList() {
        if (Objects.isNull(translatedPatientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(translatedPatientSummaryBundle).observationList();
        }
    }

    public Patient getPatient() {
        return patient;
    }
}