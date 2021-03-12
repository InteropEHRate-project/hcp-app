package eu.interopehrate.hcpapp.services.currentpatient;


import java.util.List;

public interface SendToOtherHcpService {
    @SuppressWarnings("rawtypes")
    List hcpsList();
    Boolean sendPatient(Long hcpId);
    void sendPrescription();
    void sendVitalSigns();
}
