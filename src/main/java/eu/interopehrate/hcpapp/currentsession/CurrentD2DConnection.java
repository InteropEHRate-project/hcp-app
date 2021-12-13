package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.protocols.common.DocumentCategory;
import eu.interopehrate.protocols.common.FHIRResourceCategory;
import eu.interopehrate.td2de.D2DBluetoothConnector;
import eu.interopehrate.td2de.api.D2DConnector;
import eu.interopehrate.td2de.api.TD2D;
import eu.interopehrate.td2de.api.TD2DListener;
import eu.interopehrate.td2de.api.TD2DSecureConnectionFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
public class CurrentD2DConnection implements DisposableBean {
    private final CurrentPatient currentPatient;
    private final D2DConnectionOperations d2DConnectionOperations;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private TD2D td2D;
    private D2DConnector bluetoothConnection;
    private TD2DSecureConnectionFactory secureConnectionFactory;
    private D2DConnectionState connectionState = D2DConnectionState.OFF;
    private final IndexPatientDataCommand indexPatientDataCommand;
    @Value("${ips.validator.pack}")
    private String ipsValidatorPackPath;
    private final AuditInformationService auditInformationService;
    private final Semaphore docHisSemaphore = new Semaphore(1);

    public CurrentD2DConnection(CurrentPatient currentPatient, D2DConnectionOperations d2DConnectionOperations,
                                ApplicationRuntimeInfoService applicationRuntimeInfoService, IndexPatientDataCommand indexPatientDataCommand, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.d2DConnectionOperations = d2DConnectionOperations;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
        this.indexPatientDataCommand = indexPatientDataCommand;
        this.auditInformationService = auditInformationService;
    }

    public TD2DSecureConnectionFactory getSecureConnectionFactory() {
        return secureConnectionFactory;
    }

    public IndexPatientDataCommand getIndexPatientDataCommand() {
        return indexPatientDataCommand;
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(bluetoothConnection)) {
            this.closeConnection();
        }
    }

    public void open() {
        this.connectionState = D2DConnectionState.PENDING_DEVICE;
        CompletableFuture.runAsync(this::openConnection)
                .thenRun(this::afterConnectionOpened);
    }

    public void close() {
        this.closeConnection();
    }

    public D2DConnectionState connectionState() {
        return this.connectionState;
    }

    private void openConnection() {
        try {
            bluetoothConnection = new D2DBluetoothConnector();
            this.secureConnectionFactory = bluetoothConnection.openConnection(new D2DHRExchangeListener(), this.ipsValidatorPackPath);

            this.connectionState = D2DConnectionState.ON;
            this.d2DConnectionOperations.reloadIndexPage();
        } catch (Exception e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private void afterConnectionOpened() {
        try {
            //this.td2d = this.d2DConnectionOperations.getConnection(this.secureConnectionFactory);
            td2D = secureConnectionFactory.createSecureConnection(applicationRuntimeInfoService.practitioner());
            log.info("Create secure connection");
        } catch (Exception e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        try {
            if (Objects.nonNull(bluetoothConnection) && Objects.nonNull(secureConnectionFactory)) {
                this.td2D.closeConnectionWithSEHR();
                this.bluetoothConnection.closeConnection();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.bluetoothConnection = null;
            this.secureConnectionFactory = null;
            this.td2D = null;
            this.connectionState = D2DConnectionState.OFF;
            this.indexPatientDataCommand.setCertificate(null);
            this.indexPatientDataCommand.setNoConformantJSON(false);
            this.indexPatientDataCommand.setIpsReceived(false);
            this.indexPatientDataCommand.setPrescriptionReceived(false);
            this.indexPatientDataCommand.setLaboratoryResultsReceived(false);
            this.indexPatientDataCommand.setImageReportReceived(false);
            this.indexPatientDataCommand.setPatHisReceived(false);
            this.indexPatientDataCommand.setVitalSignsReceived(false);
            IndexCommand.transmissionCompleted = false;
        }
    }

    private class D2DHRExchangeListener implements TD2DListener {
//
//        @Override
//        public void onPatientSummaryReceived(Bundle bundle) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onPatientSummaryReceived");
//                    CurrentD2DConnection.this.currentPatient.initPatientSummary(bundle);
//                    CurrentD2DConnection.this.indexPatientDataCommand.setIpsReceived(true);
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after Patient Summary was received", e);
//                }
//            });
//        }
//
//        @Override
//        public void onConsentAnswerReceived(String s) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onConsentAnswerReceived");
//                    CurrentD2DConnection.this.currentPatient.initConsent(s);
//                    CurrentD2DConnection.this.d2DConnectionOperations.auditPatientConsent();
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after consent answer was received", e);
//                }
//            });
//        }
//
//        @Override
//        public void onPrescriptionReceived(Bundle medicationRequest) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onPrescriptionReceived");
//                    CurrentD2DConnection.this.currentPatient.initPrescription(medicationRequest);
//                    CurrentD2DConnection.this.indexPatientDataCommand.setPrescriptionReceived(true);
//                    auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing Prescription Received");
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after Prescription was received", e);
//                }
//            });
//        }
//
//        @Override
//        public void onLaboratoryResultsReceived(Bundle bundle) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onLaboratoryResultsReceived");
//                    CurrentD2DConnection.this.currentPatient.initLaboratoryResults(bundle);
//                    CurrentD2DConnection.this.indexPatientDataCommand.setLaboratoryResultsReceived(true);
//                    auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing LaboratoryResults Received");
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after Prescription was received", e);
//                }
//            });
//        }
//
//        @Override
//        public void onImageReportReceived(Bundle bundle) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onImageReportReceived");
//                    CurrentD2DConnection.this.currentPatient.initImageReport(bundle);
//                    CurrentD2DConnection.this.indexPatientDataCommand.setImageReportReceived(true);
//                    auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing ImageReport Received");
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after ImageReport was received", e);
//                }
//            });
//        }
//
//        @Override
//        public void onPathologyHistoryInformationReceived(Bundle bundle) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onPathologyHistoryInformationReceived");
//                    CurrentD2DConnection.this.currentPatient.initPatHisConsultation(bundle);
//                    CurrentD2DConnection.this.indexPatientDataCommand.setPatHisReceived(true);
//                    auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing PathologyHistory Received");
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after PathologyHistoryInformation was received", e);
//                }
//            });
//        }
//
//        @Override
//        public void onMedicalDocumentConsultationReceived(Bundle bundle) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onMedicalDocumentConsultationReceived");
//                    CurrentD2DConnection.this.currentPatient.initDocHistoryConsultation(bundle);
//                    auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing DocumentConsultation Received");
//                } catch (Exception e) {
//                    log.error("Error after MedicalDocumentConsultation was received", e);
//                } finally {
//                    docHisSemaphore.release();
//                }
//            });
//        }
//
//        @Override
//        public void onVitalSignsReceived(Bundle bundle) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    log.info("onVitalSignsReceived");
//                    CurrentD2DConnection.this.currentPatient.initVitalSigns(bundle);
//                    CurrentD2DConnection.this.indexPatientDataCommand.setVitalSignsReceived(true);
//                    auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing VitalSigns Received");
//                    CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
//                } catch (Exception e) {
//                    log.error("Error after VitalSigns was received", e);
//                }
//            });
//        }

        @Override
        public boolean onCitizenPersonalDataReceived(Patient patient) {
            try {
                log.info("onPersonalIdentityReceived");
//              CurrentD2DConnection.this.connectedThread.getSignedConsent(patient);
                CurrentD2DConnection.this.currentPatient.initPatient(patient);
                CurrentD2DConnection.this.d2DConnectionOperations.auditPatientAdmission();
                CurrentD2DConnection.this.certificate();
                CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
                return Boolean.TRUE;
            } catch (Exception e) {
                log.error("Error after personal identity was received", e);
                return Boolean.FALSE;
            }
        }

        @Override
        public void onRead(Bundle healthDataBundle, int currentPage, int totalPages) {

        }

        @Override
        public void onSearch(Bundle healthDataBundle, int currentPage, int totalPages) {
            log.info("onSearch call " + " " + currentPage + " " + totalPages);
            log.info("bundle items " + healthDataBundle.getEntry().size());
            CurrentD2DConnection.this.currentPatient.initPatientSummary(healthDataBundle);
            // CurrentD2DConnection.this.currentPatient.initPatHisConsultation(healthDataBundle);
            // CurrentD2DConnection.this.currentPatient.initDocHistoryConsultation(healthDataBundle);
            CurrentD2DConnection.this.currentPatient.initImageReport(healthDataBundle);
            CurrentD2DConnection.this.currentPatient.initVitalSigns(healthDataBundle);
            if (healthDataBundle.getEntry().size() > 33) {
                CurrentD2DConnection.this.currentPatient.initLaboratoryResults(healthDataBundle);
            }
        }

        @Override
        public void onError(int errorCode, String errorMessage) {
            log.error("onNoConformantPatientSummaryReceived");
            indexPatientDataCommand.setNoConformantJSON(true);
            CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
        }

        @Override
        public void onConnectionClosure() {
            log.info("D2D connection was closed.");
        }

    }

    @SneakyThrows
    public void getLaboratoryTestsResource() {
        this.td2D.getResourcesByCategory(FHIRResourceCategory.OBSERVATION, null, true);
        CurrentD2DConnection.this.indexPatientDataCommand.setLaboratoryResultsReceived(true);
        auditInformationService.auditEvent(AuditEventType.RECEIVED_FROM_SEHR, "Auditing LaboratoryResults Received");
        log.info("LaboratoryResults received");
    }

    @SneakyThrows
    public void getPrescriptions() {
        this.td2D.getResourcesByCategory(FHIRResourceCategory.MEDICATION_REQUEST, null, false);
        log.info("Prescriptions received");
    }

    @SneakyThrows
    public void getDiagnosticImaging() {
        this.td2D.getResourcesByCategory(DocumentCategory.IMAGE_REPORT, null, false);
        log.info("Diagnostic Imaging received");
    }

    @SneakyThrows
    public void getIPS() {
        this.td2D.getResourcesByCategory(DocumentCategory.PATIENT_SUMMARY, null, false);
        log.info("Patient Summary received");
    }

    @SneakyThrows
    public void getDocumentHistory() {
        this.td2D.getResourcesByCategory(FHIRResourceCategory.DOCUMENT_REFERENCE, null, false);
        log.info("Document History received");
    }

    @SneakyThrows
    public void getPathologyHistory() {
        this.td2D.getResourcesByCategory(FHIRResourceCategory.CONDITION, null, false);
        log.info("Pathology History received");
    }

    public void certificate() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String keystore = "keystore.jks";
        char[] password = "password".toCharArray();
        String alias = "InteropEHRate";
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(keystore), password);
        java.security.cert.X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
        this.indexPatientDataCommand.setCertificate(cert.getIssuerX500Principal().getName());
    }

    public void sendMedicalDocumentRequest(LocalDate startDate, LocalDate endDate, String speciality) throws Exception {
        docHisSemaphore.acquire();
//        this.connectedThread.sendMedicalDocumentRequest(startDate, endDate, speciality);
    }

    public void waitForDocumentHistoryInit() throws InterruptedException {
        docHisSemaphore.acquire();
        docHisSemaphore.release();
    }
}