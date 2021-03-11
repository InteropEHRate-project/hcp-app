package eu.interopehrate.hcpapp.services.index;

import java.util.List;

public interface ContinueExistingVisitService {
    void retrieveEHRs(String patientId);
    void clearData();
    @SuppressWarnings("rawtypes")
    List getListOfPatients();
}
