package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.fhir.provenance.ResourceSigner;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.protocols.common.DocumentCategory;
import eu.interopehrate.protocols.common.FHIRResourceCategory;
import eu.interopehrate.r2demergency.R2DEmergencyFactory;
import eu.interopehrate.r2demergency.api.R2DEmergencyI;
import iehr.security.CryptoManagementFactory;
import iehr.security.api.CryptoManagement;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CloudConnection implements DisposableBean {
    private CloudConnectionState connectionState = CloudConnectionState.OFF;
    private final CurrentPatient currentPatient;
    private final IndexPatientDataCommand indexPatientDataCommand;
    @Value("${ips.validator.pack}")
    private String ipsValidatorPackPath;
    private R2DEmergencyI r2dEmergency;
    private String emergencyToken;
    private String bucketName;
    private final AuditInformationService auditInformationService;
    public CryptoManagement cryptoManagement;
    public final String ca_url = "http://interoperate-ejbca-service.euprojects.net";
    public final String alias = "healthorganization";
    private static final String keyStorePath = "CHU_iehr.p12";

    public CloudConnection(CurrentPatient currentPatient,
                           IndexPatientDataCommand indexPatientDataCommand, AuditInformationService auditInformationService) throws Exception {
        this.currentPatient = currentPatient;
        this.indexPatientDataCommand = indexPatientDataCommand;
        this.auditInformationService = auditInformationService;
    }

    @PostConstruct
    private void initializeR2DEmergency() {
        this.r2dEmergency = R2DEmergencyFactory.create(this.ipsValidatorPackPath);
    }

    @PostConstruct
    private void initializeCertificate() {
        this.cryptoManagement = CryptoManagementFactory.create(ca_url, keyStorePath);
    }

    @SneakyThrows
    @PostConstruct
    private void initializeCertificateS() {
        IParser parser = FhirContext.forR4().newJsonParser();
        ResourceSigner.INSTANCE.initialize("FTGM_iehr.p12", "FTGM_iehr", parser);
    }

    public String getEmergencyToken() {
        return emergencyToken;
    }

    public String getBucketName() {
        return bucketName;
    }

    public R2DEmergencyI getR2dEmergency() {
        return r2dEmergency;
    }

    @Override
    public void destroy() {
        this.closeConnection();
    }

    public void open() {
        this.connectionState = CloudConnectionState.PENDING;
    }

    public void discard() {
        this.connectionState = CloudConnectionState.OFF;
    }

    public void requestAccess(String qrCodeContent, String hospitalID, String hcoCertificate, String hcpName) throws Exception {
        this.emergencyToken = r2dEmergency.requestAccess(qrCodeContent, hospitalID, hcoCertificate, hcpName);
    }

    public void close() {
        this.closeConnection();
    }

    public CloudConnectionState connectionState() {
        return this.connectionState;
    }

    private void closeConnection() {
        this.connectionState = CloudConnectionState.OFF;
        this.indexPatientDataCommand.setIpsReceived(false);
        IndexCommand.transmissionCompleted = Boolean.FALSE;
    }

    @SneakyThrows
    public Boolean download(String qrCodeContent, String hospitalId, String hcpName) {
        try {
            if (Objects.isNull(this.emergencyToken)) {
                String hcoC = Base64.getEncoder().encodeToString(this.cryptoManagement.getUserCertificate(alias));
                this.emergencyToken = this.r2dEmergency.requestAccess(qrCodeContent, hospitalId, hcoC, hcpName);
                this.bucketName = String.valueOf(this.r2dEmergency.listBuckets(emergencyToken).get(0));
            }
            log.info("IPS requested from Cloud.");
            String patientSummary = this.r2dEmergency.get(this.emergencyToken, this.bucketName, FHIRResourceCategory.PATIENT);
            if (patientSummary.equalsIgnoreCase("File not found")) {
                log.error("PatientSummary not found");
            } else {
                log.info("IPS Received without translation & conversion from Cloud.");
                Bundle patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummary);
                List<Patient> patientList = patientSummaryBundle.getEntry()
                        .stream()
                        .map(Bundle.BundleEntryComponent::getResource)
                        .filter(resource -> resource.getResourceType().equals(ResourceType.Patient))
                        .map(Patient.class::cast)
                        .collect(Collectors.toList());
                if (patientList.size() > 0) {
                    this.currentPatient.initPatient(patientList.get(0));
                    this.currentPatient.initPatientSummary(patientSummaryBundle);
                    this.connectionState = CloudConnectionState.ON;
                    this.indexPatientDataCommand.setIpsReceived(true);
                    this.auditInformationService.auditEmergencyGetIps();
                    log.info("PatientSummary received from Cloud");
                } else {
                    this.closeConnection();
                    return Boolean.TRUE;
                }
            }

            IndexCommand.transmissionCompleted = Boolean.TRUE;
            CurrentPatient.typeOfWorkingSession = WorkingSession.EMERGENCY;
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            this.closeConnection();
            return Boolean.FALSE;
        }
    }

    public JSONArray listOfBuckets(String emergencyToken) throws Exception {
        return this.r2dEmergency.listBuckets(emergencyToken);
    }

    @SneakyThrows
    public void downloadPrescription() {
        String prescription = this.r2dEmergency.get(this.emergencyToken, this.bucketName, FHIRResourceCategory.MEDICATION_REQUEST);
        try {
            if (prescription.equalsIgnoreCase("File not found")) {
                log.error("Prescription not found");
            } else {
                Bundle prescriptionBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(prescription);
                this.currentPatient.initPrescription(prescriptionBundle);
                log.info("Prescription received from Cloud");
            }
        } catch (Exception e) {
            System.out.println("Missing file for Prescription.");
        }
    }

    @SneakyThrows
    public void downloadDocumentReference() {
        String documentReference = this.r2dEmergency.get(this.emergencyToken, this.bucketName, FHIRResourceCategory.DOCUMENT_REFERENCE);
        try {
            if (documentReference.equalsIgnoreCase("File not found")) {
                log.error("Document Reference not found");
            } else {
                Bundle documentReferenceBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(documentReference);
                this.currentPatient.initDocHistoryConsultation(documentReferenceBundle);
                log.info("Document Reference received from Cloud");
            }
        } catch (Exception e) {
            System.out.println("Missing file Document Reference.");
        }
    }

    @SneakyThrows
    public void downloadAllergies() {
        String allergy = this.r2dEmergency.get(this.emergencyToken, this.bucketName, FHIRResourceCategory.ALLERGY_INTOLERANCE);
        try {
            if (allergy.equalsIgnoreCase("File not found")) {
                log.error("Allergies not found");
            } else {
                Bundle allergyBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(allergy);
                this.currentPatient.initPatientSummary(allergyBundle);
                log.info("Allergies received from Cloud");
            }
        } catch (Exception e) {
            System.out.println("Missing file for Allergies category.");
        }
    }

    @SneakyThrows
    public void downloadCondition() {
        String condition = this.r2dEmergency.get(this.emergencyToken, this.bucketName, FHIRResourceCategory.CONDITION);
        try {
            if (condition.equalsIgnoreCase("File not found")) {
                log.error("Prescription not found");
            } else {
                Bundle conditionBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(condition);
                this.currentPatient.initPatientSummary(conditionBundle);
                log.info("Condition received from Cloud");
            }
        } catch (Exception e) {
            System.out.println("Missing file for Current Diseases");
        }
    }

    @SneakyThrows
    public void downloadLabResults() {
        String laboratoryResults = this.r2dEmergency.get(this.emergencyToken, this.bucketName, DocumentCategory.LABORATORY_REPORT);
        try {
            if (laboratoryResults.equalsIgnoreCase("File not found")) {
                log.error("LaboratoryResults not found");
            } else {
                Bundle laboratoryResultsBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(laboratoryResults);
                this.currentPatient.initLaboratoryResults(laboratoryResultsBundle);
                log.info("LaboratoryResults received from Cloud");
            }
        } catch (Exception e) {
            System.out.println("Missing file for Laboratory Results.");
        }
    }

    @SneakyThrows
    public void downloadImageReport() {
        String imageReport = this.r2dEmergency.get(this.emergencyToken, this.bucketName, DocumentCategory.IMAGE_REPORT);
        try {
            if (imageReport.equalsIgnoreCase("File not found")) {
                log.error("ImageReport not found");
            } else {
                Bundle imageReportBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(imageReport);
                this.currentPatient.initImageReport(imageReportBundle);
                log.info("ImageReport received from Cloud");
            }
        } catch (Exception e) {
            System.out.println("Missing file.");
        }
    }

    @SneakyThrows
    public String signingData() {
        PrivateKey privateKey = cryptoManagement.getPrivateKey(alias);
        byte[] certificateData = cryptoManagement.getUserCertificate(alias);
        String signed = cryptoManagement.signPayload("payload", privateKey);
        return this.cryptoManagement.createDetachedJws(certificateData, signed);
    }
}
