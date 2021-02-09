package eu.interopehrate.hcpapp.services.currentpatient;


public interface SendToOtherHcpService {
    Boolean sendCurrentPatient(Long initialHcpId) throws Exception;
    void sendEHRs();
}
