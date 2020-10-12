package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.ihs.terminalclient.fhir.TerminalFhirContext;
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
    private TerminalFhirContext terminalFhirContext;
    private Boolean displayTranslatedVersion = Boolean.TRUE;
    private Patient patient;
    private Consent consent;
    private Bundle patientSummaryBundle;
    private Bundle patientSummaryBundleTranslated;
    private Bundle prescription;
    private Bundle prescriptionTranslated;
    private Bundle laboratoryResults;
    private Bundle laboratoryResultsTranslated;
    private Certificate certificate;
    private Bundle imageReport;
    private Bundle imageReportTranslated;
    private Bundle vitalSignsBundle;
    private Bundle vitalSignsTranslated;

    public CurrentPatient(TranslateService translateService, CodesConversionService codesConversionService, TerminalFhirContext terminalFhirContext) {
        this.translateService = translateService;
        this.codesConversionService = codesConversionService;
        this.terminalFhirContext = terminalFhirContext;
    }

    public Bundle getLaboratoryResults() {
        return laboratoryResults;
    }

    public Bundle getPrescription() {
        return prescription;
    }

    public Bundle getVitalSigns() {
        return vitalSignsBundle;
    }

    public void setVitalSignsBundle(Bundle vitalSignsBundle) {
        this.vitalSignsBundle = vitalSignsBundle;
    }

    public void setVitalSignsTranslated(Bundle vitalSignsTranslated) {
        this.vitalSignsTranslated = vitalSignsTranslated;
    }

    public Bundle getPrescriptionTranslated() {
        return prescriptionTranslated;
    }

    public Bundle getVitalSignsTranslated() {
        return vitalSignsTranslated;
    }

    public void initPatient(Patient patient) {
        this.patient = patient;
    }

    public void initConsent(String consent) {
        String consentJson = consent.substring(consent.indexOf("{") + 1);
        consentJson = consentJson.substring(0, consentJson.lastIndexOf("}"));
        this.consent = (Consent) terminalFhirContext.getContext().newJsonParser().parseResource(consentJson);
    }

    public void initPatientSummary(Bundle patientSummary) {
        try {
            patientSummaryBundle = patientSummary;
            patientSummaryBundleTranslated = translateService.translate(patientSummary, Locale.ITALY, Locale.UK);
            patientSummaryBundleTranslated = codesConversionService.convert(patientSummaryBundleTranslated);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            patientSummaryBundleTranslated = patientSummary;
        }
    }

    public void initPrescription(Bundle prescription) {
        try {
            this.prescription = prescription;
            this.prescriptionTranslated = this.translateService.translate(prescription, Locale.ITALY, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.prescriptionTranslated = prescription;
        }
    }

    public void initLaboratoryResults(Bundle obs) {
        try {
            this.laboratoryResults = obs;
            this.laboratoryResultsTranslated = this.translateService.translate(obs, Locale.ITALY, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.laboratoryResultsTranslated = laboratoryResults;
        }
    }

    public void initVitalSigns(Bundle vital) {
        try {
            this.vitalSignsBundle = vital;
            this.vitalSignsTranslated = this.translateService.translate(vital, Locale.ITALY, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.vitalSignsTranslated = vitalSignsBundle;
        }

    }

    public void initImageReport(Bundle imageRep) {
        try {
            this.imageReport = imageRep;
            this.imageReportTranslated = this.translateService.translate(imageRep, Locale.ITALY, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.imageReportTranslated = this.imageReport;
        }
    }

    public void reset() {
        this.displayTranslatedVersion = Boolean.TRUE;
        this.patient = null;
        this.consent = null;
        this.patientSummaryBundle = null;
        this.patientSummaryBundleTranslated = null;
        this.prescription = null;
        this.prescriptionTranslated = null;
        this.laboratoryResults = null;
        this.laboratoryResultsTranslated = null;
        this.vitalSignsBundle = null;
        this.vitalSignsTranslated = null;
        this.imageReport = null;
        this.imageReportTranslated = null;
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        if (displayTranslatedVersion) {
            if (Objects.isNull(patientSummaryBundleTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundleTranslated).allergyIntoleranceList();
            }
        } else {
            if (Objects.isNull(patientSummaryBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundle).allergyIntoleranceList();
            }
        }
    }

    public List<Observation> laboratoryList() {
        if (displayTranslatedVersion) {
            if (Objects.isNull(laboratoryResultsTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(laboratoryResultsTranslated).laboratoryList();
            }
        } else {
            if (Objects.isNull(laboratoryResults)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(laboratoryResults).laboratoryList();
            }
        }
    }

    public List<Condition> conditionsList() {
        if (displayTranslatedVersion) {
            if (Objects.isNull(patientSummaryBundleTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundleTranslated).conditionList();
            }
        } else {
            if (Objects.isNull(patientSummaryBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundle).conditionList();
            }
        }
    }


    public List<MedicationStatement> medicationStatementList() {
        if (displayTranslatedVersion) {
            if (Objects.isNull(patientSummaryBundleTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundleTranslated).medicationStatementList();
            }
        } else {
            if (Objects.isNull(patientSummaryBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundle).medicationStatementList();
            }
        }
    }

    public List<MedicationRequest> prescriptionList() {
        if (this.displayTranslatedVersion) {
            if (Objects.isNull(this.prescriptionTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.prescriptionTranslated).prescriptionList();
            }
        } else {
            if (Objects.isNull(this.prescription)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.prescription).prescriptionList();
            }
        }
    }

    public List<Media> mediaList() {
        if (this.displayTranslatedVersion) {
            if (Objects.isNull(this.imageReportTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.imageReportTranslated).mediaList();
            }
        } else {
            if (Objects.isNull(this.imageReport)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.imageReport).mediaList();
            }
        }
    }

    public List<Observation> vitalSignsList() {
        if (displayTranslatedVersion) {
            if (Objects.isNull(vitalSignsTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(vitalSignsTranslated).vitalSignsList();
            }
        } else {
            if (Objects.isNull(vitalSignsBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(vitalSignsBundle).vitalSignsList();
            }
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