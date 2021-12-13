package eu.interopehrate.hcpapp.services.index;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import org.bouncycastle.operator.OperatorCreationException;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public interface IndexService {
    CurrentPatient getCurrentPatient();
    IndexCommand indexCommand() throws Exception;
    void openConnection();
    void closeConnection();
    void openCloudConnection();
    void discardCloudConnection();
    void closeCloudConnection();
    void requestAccess(String qrCodeContent, String hospitalID) throws Exception;
    JSONArray listBuckets(String emergencyToken) throws Exception;
    Boolean retrieveData(String qrCodeContent, String hospitalID);
    void certificate() throws CertificateException, InvalidKeyException, NoSuchAlgorithmException, KeyStoreException,
            OperatorCreationException, NoSuchProviderException, SignatureException, IOException;
}
