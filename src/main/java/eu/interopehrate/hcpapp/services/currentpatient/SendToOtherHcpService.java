package eu.interopehrate.hcpapp.services.currentpatient;


import java.io.IOException;
import java.util.List;

public interface SendToOtherHcpService {
    List hcpsList();
    Boolean sendCurrentPatient(Long initialHcpId) throws Exception;
    Boolean sendEHRs() throws IOException;
    void sendPrescription();
}
