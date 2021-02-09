package eu.interopehrate.hcpapp.services.currentpatient;


import java.util.List;

public interface SendToOtherHcpService {
    List hcpsList();
    Boolean sendCurrentPatient(Long initialHcpId) throws Exception;
    void sendEHRs();
}
