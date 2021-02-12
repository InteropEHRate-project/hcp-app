package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.ihs.terminalclient.fhir.TerminalFhirContext;
import eu.interopehrate.ihs.terminalclient.services.CodesConversionService;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private final TerminalFhirContext terminalFhirContext;
    private Boolean displayTranslatedVersion = Boolean.FALSE;
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
    private Bundle docHistoryConsult;
    private Bundle docHistoryConsultTranslated;
    private Bundle patHisBundle;
    private Bundle patHisBundleTranslated;
    @Value("${hcp.without.connection}")
    private Boolean withoutConnection;

    public CurrentPatient(TranslateService translateService, CodesConversionService codesConversionService, TerminalFhirContext terminalFhirContext) {
        this.translateService = translateService;
        this.codesConversionService = codesConversionService;
        this.terminalFhirContext = terminalFhirContext;
    }

    @PostConstruct
    private void initializeBundles() throws IOException {
        if (this.withoutConnection) {
            File file;
            try {
                file = new ClassPathResource("PatientSummary_IPS.json").getFile();
                this.patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.patientSummaryBundleTranslated = this.translateService.translate(this.patientSummaryBundle, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.patientSummaryBundleTranslated = this.patientSummaryBundle;
            }

            try {
                file = new ClassPathResource("LabResultsFromD2D.json").getFile();
                this.laboratoryResults = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.laboratoryResultsTranslated = this.translateService.translate(this.laboratoryResults, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.laboratoryResultsTranslated = this.laboratoryResults;
            }

            try {
                file = new ClassPathResource("Prescription_MedicationRequest-example.json").getFile();
                this.prescription = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.prescriptionTranslated = this.translateService.translate(this.prescription, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.prescriptionTranslated = this.prescription;
            }

            try {
                file = new ClassPathResource("DiagnosticImaging_ImageReport.json").getFile();
                this.imageReport = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.imageReportTranslated = this.translateService.translate(this.imageReport, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.imageReportTranslated = this.imageReport;
            }

            try {
                file = new ClassPathResource("VitalSignsExample.json").getFile();
                this.vitalSignsBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.vitalSignsTranslated = this.translateService.translate(this.vitalSignsBundle, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.vitalSignsTranslated = this.vitalSignsBundle;
            }

            try {
                file = new ClassPathResource("PathologyHistoryCompositionExampleIPS.json").getFile();
                this.patHisBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.patHisBundleTranslated = this.translateService.translate(this.patHisBundle, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.patHisBundleTranslated = this.patHisBundle;
            }

            try {
                file = new ClassPathResource("MedicalDocumentReferenceExampleBundle2.json").getFile();
                this.docHistoryConsult = (Bundle) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
                this.docHistoryConsultTranslated = this.translateService.translate(this.docHistoryConsult, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.docHistoryConsultTranslated = this.docHistoryConsult;
            }

            file = new ClassPathResource("PatientDataExample.json").getFile();
            this.patient = (Patient) FhirContext.forR4().newJsonParser().parseResource(Files.readString(file.toPath()));
        }
    }

    public Boolean getWithoutConnection() {
        return withoutConnection;
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

    public Certificate getCertificate() {
        return certificate;
    }

    public String getCertificateAsString() {
        return certificate.toString();
    }

    public Bundle getPatientSummaryBundle() {
        return patientSummaryBundle;
    }

    public Bundle getLaboratoryResults() {
        return laboratoryResults;
    }

    public Bundle getImageReport() {
        return imageReport;
    }

    public Bundle getDocHistoryConsult() {
        return docHistoryConsult;
    }

    public Bundle getPatHisBundle() {
        return patHisBundle;
    }

    public Bundle getPrescription() {
        return prescription;
    }

    public Bundle getVitalSigns() {
        return vitalSignsBundle;
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
            patientSummaryBundleTranslated = translateService.translate(patientSummary, Locale.UK);
            patientSummaryBundleTranslated = codesConversionService.convert(patientSummaryBundleTranslated);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            patientSummaryBundleTranslated = patientSummary;
        }
    }

    public void initPrescription(Bundle prescription) {
        try {
            this.prescription = prescription;
            this.prescriptionTranslated = this.translateService.translate(prescription, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.prescriptionTranslated = prescription;
        }
    }

    public void initLaboratoryResults(Bundle obs) {
        try {
            this.laboratoryResults = obs;
            this.laboratoryResultsTranslated = this.translateService.translate(obs, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.laboratoryResultsTranslated = laboratoryResults;
        }
    }

    public void initVitalSigns(Bundle vital) {
        try {
            this.vitalSignsBundle = vital;
            this.vitalSignsTranslated = this.translateService.translate(vital, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.vitalSignsTranslated = vitalSignsBundle;
        }

    }

    public void initImageReport(Bundle imageRep) {
        try {
            this.imageReport = imageRep;
            this.imageReportTranslated = this.translateService.translate(imageRep, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.imageReportTranslated = this.imageReport;
        }
    }

    public void initDocHistoryConsultation(Bundle docHistoryConsultation) {
        try {
            this.docHistoryConsult = docHistoryConsultation;
            this.docHistoryConsultTranslated = this.translateService.translate(docHistoryConsultation, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.docHistoryConsultTranslated = this.docHistoryConsult;
        }
    }

    public void initPatHisConsultation(Bundle patHisConsultation) {
        try {
            this.patHisBundle = patHisConsultation;
            this.patHisBundleTranslated = this.translateService.translate(patHisConsultation, Locale.UK);
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.patHisBundleTranslated = this.patHisBundle;
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
        this.docHistoryConsult = null;
        this.docHistoryConsultTranslated = null;
        this.patHisBundle = null;
        this.patHisBundleTranslated = null;
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

    public List<DocumentReference> docHistoryConsultationList() {
        if (this.displayTranslatedVersion) {
            if (Objects.isNull(this.docHistoryConsultTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.docHistoryConsultTranslated).docHistoryConsultationList();
            }
        } else {
            if (Objects.isNull(this.docHistoryConsult)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.docHistoryConsult).docHistoryConsultationList();
            }
        }
    }

    public List<Observation> patHisConsultationRiskFactorsList() {
        if (this.displayTranslatedVersion) {
            if (Objects.isNull(this.patHisBundleTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.patHisBundleTranslated).patHisConsultationObservationsList();
            }
        } else {
            if (Objects.isNull(this.patHisBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.patHisBundle).patHisConsultationObservationsList();
            }
        }
    }

    public List<Condition> patHisConsultationDiagnosesList() {
        if (this.displayTranslatedVersion) {
            if (Objects.isNull(this.patHisBundleTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.patHisBundleTranslated).patHisConsultationConditionsList();
            }
        } else {
            if (Objects.isNull(this.patHisBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.patHisBundle).patHisConsultationConditionsList();
            }
        }
    }

    public void setDisplayTranslatedVersion(Boolean displayTranslatedVersion) {
        this.displayTranslatedVersion = displayTranslatedVersion;
    }

    public static String extractExtensionText(Coding coding, CurrentPatient currentPatient) {
        if (currentPatient.getDisplayTranslatedVersion()
                && coding.hasDisplayElement()
                && coding.getDisplayElement().hasExtension()
                && coding.getDisplayElement().getExtensionFirstRep().hasExtension() && coding.getDisplayElement().getExtensionFirstRep().getExtension().size() >= 2) {
            return coding.getDisplayElement().getExtensionFirstRep().getExtension().get(1).getValue().toString();
        } else {
            return coding.getDisplay();
        }
    }
}