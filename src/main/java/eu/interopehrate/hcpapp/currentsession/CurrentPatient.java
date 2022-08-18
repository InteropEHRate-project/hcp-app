package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import eu.interopehrate.hcpapp.services.index.impl.IndexServiceImpl;
import eu.interopehrate.ihs.terminalclient.fhir.TerminalFhirContext;
import eu.interopehrate.ihs.terminalclient.services.CodesConversionService;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.Certificate;
import java.util.*;

@Component
public class CurrentPatient {
    private static final Logger logger = LoggerFactory.getLogger(CurrentPatient.class);
    public static WorkingSession typeOfWorkingSession;
    private final TranslateService translateService;
    private final CodesConversionService codesConversionService;
    private final TerminalFhirContext terminalFhirContext;
    private Boolean displayTranslatedVersion = Boolean.TRUE;
    private Patient patient;
    private Consent consent;
    private Bundle patientSummaryBundle;
    private Bundle patientSummaryBundleTranslated;
    private Bundle patientSummaryBundleSent;
    private Bundle patientSummaryBundleTranslatedSent;
    private Bundle patientAllergiesBundle;
    private Bundle patientAllergiesBundleTranslated;
    private Bundle prescription;
    private Bundle prescriptionTranslated;
    private Bundle laboratoryResults;
    private Bundle laboratoryResultsTranslated;
    private Certificate certificate;
    private Bundle imageReport;
    private Bundle imageReportTranslated;
    private Bundle diagnosticReport;
    private Bundle diagnosticReportTranslated;
    private Bundle vitalSignsBundle;
    private Bundle vitalSignsTranslated;
    private Bundle docHistoryConsult;
    private Bundle docHistoryConsultTranslated;
    private Bundle patHisBundle;
    private Bundle patHisBundleTranslated;
    @Value("${hcp.without.connection}")
    private Boolean withoutConnection;
    @Autowired
    private IndexServiceImpl indexService;

    public CurrentPatient(TranslateService translateService, CodesConversionService codesConversionService, TerminalFhirContext terminalFhirContext) {
        this.translateService = translateService;
        this.codesConversionService = codesConversionService;
        this.terminalFhirContext = terminalFhirContext;
    }

    public void setDisplayTranslatedVersion(Boolean displayTranslatedVersion) {
        this.displayTranslatedVersion = displayTranslatedVersion;
    }

    public void setPatientSummaryBundleTranslated(Bundle patientSummaryBundleTranslated) {
        this.patientSummaryBundleTranslated = patientSummaryBundleTranslated;
    }

    public void setPrescriptionTranslated(Bundle prescriptionTranslated) {
        this.prescriptionTranslated = prescriptionTranslated;
    }

    public void setLaboratoryResultsTranslated(Bundle laboratoryResultsTranslated) {
        this.laboratoryResultsTranslated = laboratoryResultsTranslated;
    }

    public void setImageReportTranslated(Bundle imageReportTranslated) {
        this.imageReportTranslated = imageReportTranslated;
    }

    public void setVitalSignsTranslated(Bundle vitalSignsTranslated) {
        this.vitalSignsTranslated = vitalSignsTranslated;
    }

    public void setDocHistoryConsultTranslated(Bundle docHistoryConsultTranslated) {
        this.docHistoryConsultTranslated = docHistoryConsultTranslated;
    }

    public void setPatHisBundleTranslated(Bundle patHisBundleTranslated) {
        this.patHisBundleTranslated = patHisBundleTranslated;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setPatientSummaryBundle(Bundle patientSummaryBundle) {
        this.patientSummaryBundle = patientSummaryBundle;
    }

    public void setPrescription(Bundle prescription) {
        this.prescription = prescription;
    }

    public void setLaboratoryResults(Bundle laboratoryResults) {
        this.laboratoryResults = laboratoryResults;
    }

    public void setImageReport(Bundle imageReport) {
        this.imageReport = imageReport;
    }

    public void setVitalSignsBundle(Bundle vitalSignsBundle) {
        this.vitalSignsBundle = vitalSignsBundle;
    }

    public void setDocHistoryConsult(Bundle docHistoryConsult) {
        this.docHistoryConsult = docHistoryConsult;
    }

    public void setPatHisBundle(Bundle patHisBundle) {
        this.patHisBundle = patHisBundle;
    }

    @PostConstruct
    private void initializeBundles() {
        if (this.withoutConnection) {
            IndexCommand.transmissionCompleted = true;
            InputStream in;
            in = getClass().getResourceAsStream("/PatientDataExample.json");
            this.patient = (Patient) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
            try {
                in = getClass().getResourceAsStream("/PatientSummary_IPS.json");
                this.patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.patientSummaryBundleTranslated = this.translateService.translate(this.patientSummaryBundle, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.patientSummaryBundleTranslated = this.patientSummaryBundle;
            }

            try {
                in = getClass().getResourceAsStream("/LabResultsFromD2D.json");
                this.laboratoryResults = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.laboratoryResultsTranslated = this.translateService.translate(this.laboratoryResults, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.laboratoryResultsTranslated = this.laboratoryResults;
            }

            try {
                in = getClass().getResourceAsStream("/Prescription_MedicationRequest-example.json");
                this.prescription = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.prescriptionTranslated = this.translateService.translate(this.prescription, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.prescriptionTranslated = this.prescription;
            }

            try {
                in = getClass().getResourceAsStream("/DiagnosticImaging_ImageReport.json");
                this.imageReport = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.imageReportTranslated = this.translateService.translate(this.imageReport, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.imageReportTranslated = this.imageReport;
            }

            try {
                in = getClass().getResourceAsStream("/VitalSignsExample.json");
                this.vitalSignsBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.vitalSignsTranslated = this.translateService.translate(this.vitalSignsBundle, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.vitalSignsTranslated = this.vitalSignsBundle;
            }

            try {
                in = getClass().getResourceAsStream("/PathologyHistoryCompositionExampleIPS-ITA.json");
                this.patHisBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.patHisBundleTranslated = this.translateService.translate(this.patHisBundle, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.patHisBundleTranslated = this.patHisBundle;
            }

            try {
                in = getClass().getResourceAsStream("/MedicalDocumentReferenceExampleBundle2.json");
                this.docHistoryConsult = (Bundle) FhirContext.forR4().newJsonParser().parseResource(inputStreamToString(in));
                this.docHistoryConsultTranslated = this.translateService.translate(this.docHistoryConsult, Locale.UK);
            } catch (Exception e) {
                logger.error("Error calling translation service.", e);
                this.docHistoryConsultTranslated = this.docHistoryConsult;
            }
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

    public Bundle getPatientSummaryBundleTranslated() {
        return patientSummaryBundleTranslated;
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

    public Bundle getPatHisBundleTranslated() {
        return patHisBundleTranslated;
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
            patientSummaryBundle = new Bundle();
            for (Bundle.BundleEntryComponent entry : patientSummary.getEntry()) {
                if (entry.getResource() instanceof Provenance)
                    continue;

                else if (entry.getResource() instanceof Media) {
                    Media originalMedia = (Media) entry.getResource();
                    Media emptyMedia = new Media();
                    Meta meta = new Meta();
                    meta.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Media-IEHR");
                    emptyMedia.setMeta(meta);
                    emptyMedia.setId(originalMedia.getId());
                    emptyMedia.setStatus(Media.MediaStatus.COMPLETED);
                    emptyMedia.setSubject(originalMedia.getSubject());
                    emptyMedia.setOperator(originalMedia.getOperator());
                    emptyMedia.setEncounter(originalMedia.getEncounter());
                    emptyMedia.addNote().setText(originalMedia.getNote().toString());
                    patientSummaryBundle.addEntry().setResource(emptyMedia);

                } else {
                    patientSummaryBundle.addEntry().setResource(entry.getResource());
                }
            }
            patientSummaryBundleTranslated = translateService.translate(patientSummaryBundle, new Locale("el"));
            //patientSummaryBundleTranslated = codesConversionService.convert(patientSummaryBundleTranslated);
            logger.info("IPS translated & converted.");
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            patientSummaryBundleTranslated = patientSummary;
        }
    }

    public Bundle initPatientSummarySent(Bundle patientSummary) {
        try {
            patientSummaryBundleSent = new Bundle();
            for (Bundle.BundleEntryComponent entry : patientSummary.getEntry()) {
                if (entry.getResource() instanceof Provenance)
                    continue;

                else if (entry.getResource() instanceof Media) {
                    Media originalMedia = (Media) entry.getResource();
                    Media emptyMedia = new Media();
                    Meta meta = new Meta();
                    meta.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Media-IEHR");
                    emptyMedia.setMeta(meta);
                    emptyMedia.setId(originalMedia.getId());
                    emptyMedia.setStatus(Media.MediaStatus.COMPLETED);
                    emptyMedia.setSubject(originalMedia.getSubject());
                    emptyMedia.setOperator(originalMedia.getOperator());
                    emptyMedia.setEncounter(originalMedia.getEncounter());
                    emptyMedia.addNote().setText(originalMedia.getNote().toString());
                    patientSummaryBundleSent.addEntry().setResource(emptyMedia);

                } else {
                    patientSummaryBundleSent.addEntry().setResource(entry.getResource());
                }
            }
            patientSummaryBundleTranslatedSent = translateService.translate(patientSummaryBundleSent, new Locale("fr"));
            logger.info("IPS translated & converted.");
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            patientSummaryBundleTranslatedSent = patientSummary;
        }
        return patientSummaryBundleTranslatedSent;
    }

    public void initAllergiesEmergency(Bundle allergies) {
        try {
            this.patientAllergiesBundle = allergies;
            this.patientAllergiesBundleTranslated = this.translateService.translate(patientAllergiesBundle, new Locale("el"));
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.patientAllergiesBundleTranslated = this.patientAllergiesBundle;
        }
    }

    public void initPrescription(Bundle prescript) {
        try {
            this.prescription = prescript;
            this.prescriptionTranslated = patientSummaryBundleTranslated;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.prescriptionTranslated = this.prescription;
        }
    }

    public void initPrescriptionEmergency(Bundle prescript) {
        try {
            this.prescription = prescript;
            this.prescriptionTranslated = this.translateService.translate(prescription, new Locale("el"));
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.prescriptionTranslated = this.prescription;
        }
    }

    public void initLaboratoryResults(Bundle obs) {
        try {
            this.laboratoryResults = obs;
            this.laboratoryResultsTranslated = patientSummaryBundleTranslated;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.laboratoryResultsTranslated = laboratoryResults;
        }
    }

    public void initLaboratoryResultsEmergency(Bundle obs) {
        try {
            this.laboratoryResults = obs;
            this.laboratoryResultsTranslated = this.translateService.translate(laboratoryResults, new Locale("el"));
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.laboratoryResultsTranslated = laboratoryResults;
        }
    }

    public void initVitalSigns(Bundle vital) {
        try {
            this.vitalSignsBundle = vital;
            this.vitalSignsTranslated = patientSummaryBundleTranslated;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.vitalSignsTranslated = vitalSignsBundle;
        }
    }

    public void initVitalSignsEmergency(Bundle vital) {
        try {
            this.vitalSignsBundle = vital;
            this.vitalSignsTranslated = this.translateService.translate(vitalSignsBundle, new Locale("el"));
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.vitalSignsTranslated = vitalSignsBundle;
        }
    }

    public void initImageReport(Bundle imageRep) {
        try {
            this.imageReport = imageRep;
            this.imageReportTranslated = imageRep;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.imageReportTranslated = this.imageReport;
        }
    }

    public void initDiagnosticReport(Bundle diagnosticRep) {
        try {
            this.diagnosticReport = diagnosticRep;
            this.diagnosticReportTranslated = patientSummaryBundleTranslated;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.diagnosticReportTranslated = this.diagnosticReport;
        }
    }

    public void initDocHistoryConsultation(Bundle docHistoryConsultation) {
        try {
            this.docHistoryConsult = docHistoryConsultation;
            this.docHistoryConsultTranslated = patientSummaryBundleTranslated;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.docHistoryConsultTranslated = this.docHistoryConsult;
        }
    }

    public void initPatHisConsultation(Bundle patHisConsultation) {
        try {
            this.patHisBundle = patHisConsultation;
            this.patHisBundleTranslated = patientSummaryBundleTranslated;
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.patHisBundleTranslated = this.patHisBundle;
        }
    }

    public void initPatHisConsultationEmergency(Bundle patHisConsultation) {
        try {
            this.patHisBundle = patHisConsultation;
            this.patHisBundleTranslated = this.translateService.translate(patHisBundle, new Locale("el"));
        } catch (Exception e) {
            logger.error("Error calling translation service.", e);
            this.patHisBundleTranslated = this.patHisBundle;
        }
    }

    @SneakyThrows
    public void reset() {
        this.displayTranslatedVersion = Boolean.TRUE;
        this.patient = null;
        this.consent = null;
        this.certificate = null;
        this.indexService.indexCommand().getPatientDataCommand().setAge(null);
        this.indexService.indexCommand().getPatientDataCommand().setConsent(null);
        this.indexService.indexCommand().getPatientDataCommand().setCertificate(null);
        this.indexService.indexCommand().getPatientDataCommand().setLastName(null);
        this.indexService.indexCommand().getPatientDataCommand().setFirstName(null);
        this.indexService.indexCommand().getPatientDataCommand().setPhoto(null);
        this.indexService.indexCommand().getPatientDataCommand().setGender(null);
        this.indexService.indexCommand().getPatientDataCommand().setCountry(null);
        this.indexService.indexCommand().setQrCode(null);
        this.indexService.indexCommand().setHospitalID(null);
        this.indexService.indexCommand().setHcpName(null);
        this.indexService.indexCommand().setBucketName(null);
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
        typeOfWorkingSession = null;
        IndexCommand.transmissionCompleted = Boolean.FALSE;
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        if (Objects.isNull(patientSummaryBundle)) {
            return Collections.emptyList();
        } else if (!Objects.nonNull(patientSummaryBundle)) {
            if (Objects.isNull(patientAllergiesBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientAllergiesBundle).allergyIntoleranceList();
            }
        } else {
            return new BundleProcessor(patientSummaryBundle).allergyIntoleranceList();
        }
    }

    public List<AllergyIntolerance> allergyIntoleranceTranslatedList() {
        if (Objects.isNull(patientSummaryBundleTranslated)) {
            return Collections.emptyList();
        } else if (!Objects.isNull(patientSummaryBundleTranslated)) {
            if (Objects.isNull(patientSummaryBundle)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(patientSummaryBundleTranslated).allergyIntoleranceList();
            }
        } else if (Objects.isNull(patientAllergiesBundleTranslated)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientAllergiesBundleTranslated).allergyIntoleranceList();
        }
    }

    //Original method
//    public List<Observation> laboratoryList() {
//        if (displayTranslatedVersion) {
//            if (Objects.isNull(laboratoryResultsTranslated)) {
//                return Collections.emptyList();
//            } else {
//                return new BundleProcessor(laboratoryResultsTranslated).laboratoryList();
//            }
//        } else {
//            if (Objects.isNull(laboratoryResults)) {
//                return Collections.emptyList();
//            } else {
//                return new BundleProcessor(laboratoryResults).laboratoryList();
//            }
//        }
//    }

    public List<Observation> laboratoryList() {
        if (Objects.isNull(laboratoryResultsTranslated)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(laboratoryResultsTranslated).laboratoryList();
        }
    }

    public List<Condition> conditionsList() {
        if (Objects.isNull(patientSummaryBundleTranslated)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundleTranslated).conditionList();
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

    public List<MedicationStatement> prescriptionList() {
        if (Objects.isNull(this.prescriptionTranslated)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(this.prescriptionTranslated).prescriptionList();
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

    public List<DiagnosticReport> diagnosticReportList() {
        if (this.displayTranslatedVersion) {
            if (Objects.isNull(this.diagnosticReportTranslated)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.diagnosticReportTranslated).diagnosticReportList();
            }
        } else {
            if (Objects.isNull(this.diagnosticReport)) {
                return Collections.emptyList();
            } else {
                return new BundleProcessor(this.diagnosticReport).diagnosticReportList();
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
        if (Objects.isNull(this.patHisBundleTranslated)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(this.patHisBundleTranslated).patHisConsultationObservationsList();
        }
    }

    public List<Condition> patHisConsultationDiagnosesList() {
        if (Objects.isNull(this.patHisBundleTranslated)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(this.patHisBundleTranslated).patHisConsultationConditionsList();
        }
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

    // HARCODED METHOD that doesn't take into account the value of "displayTranaslatedVersion" boolean
    public static String testExtractExtensionText(Coding coding) {
        if (coding.hasDisplayElement()
                && coding.getDisplayElement().hasExtension()
                && coding.getDisplayElement().getExtensionFirstRep().hasExtension() && coding.getDisplayElement().getExtensionFirstRep().getExtension().size() >= 2) {
            return coding.getDisplayElement().getExtensionFirstRep().getExtension().get(1).getValue().toString();
        } else {
            return coding.getDisplay();
        }
    }

    @SneakyThrows
    private static String inputStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList<Character> chars = new ArrayList<>();
        int i;
        while ((i = reader.read()) != -1) {
            chars.add((char) i);
        }
        reader.close();
        char[] array = new char[chars.size()];
        int counter = 0;
        for (Character c : chars) {
            array[counter++] = c;
        }
        return new String(array);
    }
}