package eu.interopehrate.hcpapp.services.index;

public interface ContinueExistingVisitService {
    void retrieveEHRs(String patientId);
    void clearData();
}
