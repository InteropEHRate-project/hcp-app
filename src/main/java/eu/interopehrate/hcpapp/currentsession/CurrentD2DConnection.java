package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import eu.interopehrate.td2de.api.D2DConnectionListeners;
import eu.interopehrate.td2de.api.D2DHRExchangeListeners;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class CurrentD2DConnection implements DisposableBean {
    private final CurrentPatient currentPatient;
    private final D2DConnectionOperations d2DConnectionOperations;
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;
    private D2DConnectionState connectionState = D2DConnectionState.OFF;
    private IndexPatientDataCommand indexPatientDataCommand;
    @Value("${ips.validator.pack}")
    private String ipsValidatorPackPath;

    public IndexPatientDataCommand getIndexPatientDataCommand() {
        return indexPatientDataCommand;
    }

    public CurrentD2DConnection(CurrentPatient currentPatient,
                                D2DConnectionOperations d2DConnectionOperations, IndexPatientDataCommand indexPatientDataCommand) {
        this.currentPatient = currentPatient;
        this.d2DConnectionOperations = d2DConnectionOperations;
        this.indexPatientDataCommand = indexPatientDataCommand;
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(bluetoothConnection)) {
            this.closeConnection();
        }
    }

    public void open() {
        this.connectionState = D2DConnectionState.PENDING_DEVICE;
        this.indexPatientDataCommand.setNoConformantJSON(false);
        this.indexPatientDataCommand.setIpsReceived(false);
        this.indexPatientDataCommand.setPrescriptionReceived(false);
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
            bluetoothConnection = new BluetoothConnection();
            connectedThread = bluetoothConnection.listenConnection(new D2DHRExchangeListener(), new D2DConnectionListener(), this.ipsValidatorPackPath);
            this.connectionState = D2DConnectionState.ON;
            this.d2DConnectionOperations.reloadIndexPage();
        } catch (IOException e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private void afterConnectionOpened() {
        try {
            this.d2DConnectionOperations.sendPractitionerIdentity(this.connectedThread);
            this.connectedThread.sendHCPCertificate();
        } catch (Exception e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        try {
            if (Objects.nonNull(bluetoothConnection) && Objects.nonNull(connectedThread)) {
                this.bluetoothConnection.closeConnection();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.bluetoothConnection = null;
            this.connectedThread = null;
            this.connectionState = D2DConnectionState.OFF;
        }
    }

    private class D2DConnectionListener implements D2DConnectionListeners {
        @Override
        public void onConnectionClosure() {
            log.info("D2D connection was closed.");
        }
    }

    private class D2DHRExchangeListener implements D2DHRExchangeListeners {
        @Override
        public void onPersonalIdentityReceived(Patient patient) {
            try {
                CurrentD2DConnection.this.connectedThread.getSignedConsent(patient);
                CurrentD2DConnection.this.currentPatient.initPatient(patient);
                CurrentD2DConnection.this.d2DConnectionOperations.auditPatientAdmission();
                CurrentD2DConnection.this.certificate();
                CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
            } catch (Exception e) {
                log.error("Error after personal identity was received", e);
            }
        }

        @Override
        public void onPatientSummaryReceived(Bundle bundle) {
            try {
                log.info("onPatientSummaryReceived");
                CurrentD2DConnection.this.currentPatient.initPatientSummary(bundle);

                File file = new ClassPathResource("samples_StructuredLaboratoryResult_V1.json").getFile();
                String initialJsonFhir = Files.readString(file.toPath());
                FhirContext fc = FhirContext.forR4();
                IParser parser = fc.newJsonParser().setPrettyPrint(true);
                Bundle observation = (Bundle) parser.parseResource(initialJsonFhir);
                List<Observation> observationList = new BundleProcessor(observation).observationList();
                CurrentD2DConnection.this.currentPatient.initLaboratoryResults(new BundleProcessor(observation).observationList());

                CurrentD2DConnection.this.indexPatientDataCommand.setIpsReceived(true);
                CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
            } catch (Exception e) {
                log.error("Error after Patient Summary was received", e);
            }
        }

        @Override
        public void onConsentAnswerReceived(String s) {
            try {
                CurrentD2DConnection.this.currentPatient.initConsent(s);
                CurrentD2DConnection.this.d2DConnectionOperations.auditPatientConsent();
                CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
            } catch (Exception e) {
                log.error("Error after consent answer was received", e);
            }
        }

        @Override
        public void onNoConformantPatientSummaryReceived() {
            log.error("onNoConformantPatientSummaryReceived");
            indexPatientDataCommand.setNoConformantJSON(true);
            CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
        }

        @Override
        public void onPrescriptionReceived(MedicationRequest medicationRequest) {
            try {
                log.info("onPrescriptionReceived");
                CurrentD2DConnection.this.currentPatient.initPrescription(medicationRequest);
                CurrentD2DConnection.this.indexPatientDataCommand.setPrescriptionReceived(true);
                CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
            } catch (Exception e) {
                log.error("Error after Prescription was received", e);
            }
        }

        @Override
        public void onNoConformantPrescriptionReceived() {
            log.error("onNoConformantPrescriptionReceived");
            indexPatientDataCommand.setNoConformantJSON(true);
            CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
        }
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
}