package eu.interopehrate.hcpapp.d2dlibrary;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.api.D2DConnection;
import eu.interopehrate.td2de.api.D2DHRExchangeListeners;
import eu.interopehrate.td2de.api.D2DSecurityConnection;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class D2DLibraryTests {

    @Test
    public void testGetBTAdapterAddress() throws Exception {
        D2DConnection d2DConnection = new BluetoothConnection();
        ((D2DSecurityConnection) d2DConnection).fetchCertificate();
        String btAdapterAddress = d2DConnection.getBTadapterAddress();
        Assert.assertNotNull(btAdapterAddress);
        System.out.println(btAdapterAddress);
    }

    @Test
    public void testOnNoConformantPatientSummaryReceived() {
        class D2DHRExchangeListenersTestImpl implements D2DHRExchangeListeners {
            @Override
            public void onPersonalIdentityReceived(Patient patient) {

            }

            @Override
            public void onPatientSummaryReceived(Bundle bundle) {

            }

            @Override
            public void onConsentAnswerReceived(String s) {

            }

            @Override
            public void onNoConformantPatientSummaryReceived() {

            }
        }

        D2DHRExchangeListeners d2DHRExchangeListeners = new D2DHRExchangeListenersTestImpl();
        D2DLibraryMock d2DLibraryMock = new D2DLibraryMock(d2DHRExchangeListeners);
        d2DLibraryMock.simulateNonValidPatientSummaryReceived();
    }

    @Test
    public void testLoadPublicKey() throws Exception {
        D2DConnection d2DConnection = new BluetoothConnection();
        ((D2DSecurityConnection) d2DConnection).fetchCertificate();
        PublicKey publicKey = BluetoothConnection.loadPublicKey("mykey", "password".toCharArray(), "keystore.jks");
        PrivateKey privateKey = BluetoothConnection.loadPrivateKey("mykey", "password".toCharArray(), "keystore.jks");
        System.out.println();
    }

    private static class D2DLibraryMock {
        private D2DHRExchangeListeners listeners;

        public D2DLibraryMock(D2DHRExchangeListeners listeners) {
            this.listeners = listeners;
        }

        public void simulateNonValidPatientSummaryReceived() {
            listeners.onNoConformantPatientSummaryReceived();
        }
    }

    @Test
    public void createCertif() throws OperatorCreationException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
        System.out.println(cert);
    }
}
