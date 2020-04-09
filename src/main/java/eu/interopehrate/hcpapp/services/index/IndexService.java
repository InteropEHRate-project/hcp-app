package eu.interopehrate.hcpapp.services.index;

import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public interface IndexService {
    IndexCommand indexCommand() throws Exception;

    void openConnection();

    void closeConnection();

    void certificate() throws CertificateException, InvalidKeyException, NoSuchAlgorithmException, KeyStoreException, OperatorCreationException, NoSuchProviderException, SignatureException, IOException;
}
