package eu.interopehrate.hcpapp.d2dlibrary;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.td2de.D2DBluetoothConnector;
import eu.interopehrate.td2de.api.D2DConnector;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class D2DLibraryTests {

    @Test
    public void testGetBTAdapterAddress() throws Exception {
        D2DConnector d2DConnection = new D2DBluetoothConnector();
//        ((D2DSecurityConnection) d2DConnection).fetchCertificate();
        String btAdapterAddress = d2DConnection.getBtAdapterAddress();
        Assert.assertNotNull(btAdapterAddress);
        System.out.println(btAdapterAddress);
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

    @Test
    public void testSendPrescription() {
        Bundle prescription = new Bundle();
        String prescriptionJSON = FhirContext.forR4().newJsonParser().encodeResourceToString(prescription);
        this.write("medicationRequest#ACK#" + prescriptionJSON);
    }

    @Test
    public void testSendVitalSigns() {
        Bundle vitalSigns = new Bundle();
        String vitalSignsJSON = FhirContext.forR4().newJsonParser().encodeResourceToString(vitalSigns);
        this.write("vitalSigns#ACK#" + vitalSignsJSON);
    }

    @Test
    public void testSendDiagnosticConclusion() {
        Bundle diagnosticConclusion = new Bundle();
        String diagnosticConclusionJSON = FhirContext.forR4().newJsonParser().encodeResourceToString(diagnosticConclusion);
        this.write("diagnosticConclusion#ACK#" + diagnosticConclusionJSON);
    }

    @Test
    public void testSendInstrumentalExamination() {
        Bundle instrumentalExamination = new Bundle();
        String instrumentalExaminationJSON = FhirContext.forR4().newJsonParser().encodeResourceToString(instrumentalExamination);
        this.write("instrumentalExamination#ACK#" + instrumentalExaminationJSON);
    }


    private void write(String data) {
        DataOutputStream dos = new DataOutputStream(new ByteArrayOutputStream());
        try {
            dos.writeUTF(data + "\n");
            dos.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
