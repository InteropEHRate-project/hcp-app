package eu.interopehrate.hcpapp.services.index;

public interface ContinueExistingVisitService {
    void retrieveEHRs(Long patientId);

    void clearData();
}
