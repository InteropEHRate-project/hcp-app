package eu.interopehrate.hcpapp.services.currentpatient;


import java.io.IOException;
import java.util.List;

public interface SendToOtherHcpService {
    @SuppressWarnings("rawtypes")
    List hcpsList();
    Boolean sendCurrentPatient(Long hcpId) throws Exception;
    Boolean sendEHRs() throws IOException;
    void sendPrescription();
    void sendVitalSigns();
}
