package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import eu.interopehrate.td2de.api.D2DConnectionListeners;
import eu.interopehrate.td2de.api.D2DHRExchangeListeners;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
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
            connectedThread = bluetoothConnection.listenConnection(new D2DHRExchangeListener(), new D2DConnectionListener());
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
                CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
            } catch (Exception e) {
                log.error("Error after personal identity was received", e);
            }
        }

        @Override
        public void onPatientSummaryReceived(Bundle bundle) {
            try {
                CurrentD2DConnection.this.currentPatient.initPatientSummary(bundle);
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
    }
    public void certificate (byte[] tempCert) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException, SignatureException, InvalidKeyException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        String issuerString = "C=IT, O=InteropEHRate, OU=InteropEHRate Certificate, CN=Mario Rossi, UID= 0f3e03e0-b4ca-4a76-821d-bdef16267ed0 ";
        String subjectString = "C=IT, O=InteropEHRate, OU=InteropEHRate Certificate, CN=Mario Rossi, UID= 0f3e03e0-b4ca-4a76-821d-bdef16267ed0 ";
        X500Name issuer = new X500Name(issuerString);
        BigInteger serial = BigInteger.ONE;
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + (365 * 24 * 60 * 60));
        X500Name subject = new X500Name(subjectString);
        PublicKey publicKey = keyPair.getPublic();
        JcaX509v3CertificateBuilder v3CertificateBuilder = new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKey);
        X509CertificateHolder certHolder = v3CertificateBuilder.build(new JcaContentSignerBuilder("SHA1WithRSA").build(keyPair.getPrivate()));
        X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);
        cert.checkValidity(new Date());
        cert.verify(keyPair.getPublic());

        CertificateFactory certificateFactory = CertificateFactory.getInstance("jks");
        java.security.cert.Certificate trustedCertif = certificateFactory.generateCertificate(new ByteArrayInputStream(tempCert));
        System.out.println("Received Public: "+ trustedCertif.getPublicKey().toString());

        String alias ="mykey";
        String keystore = "keystore.jks";
        KeyStore keyStore = KeyStore.getInstance("jsk");
        keyStore.load(null,null);
        keyStore.setCertificateEntry("InteropEHRate",trustedCertif);
        keyStore.getCertificate(alias);

        Principal principal = cert.getIssuerX500Principal();
        principal.getName();
    }
}