package eu.interopehrate.hcpapp.services.index;

import java.util.List;

public interface InpatientEncounterService {
    void retrieveEHRs(String patientId);
    @SuppressWarnings("rawtypes")
    List getListOfPatients();
}
