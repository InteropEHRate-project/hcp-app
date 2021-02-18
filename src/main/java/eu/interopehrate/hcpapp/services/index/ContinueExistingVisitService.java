package eu.interopehrate.hcpapp.services.index;

import org.hl7.fhir.r4.model.Patient;

public interface ContinueExistingVisitService {
    Patient retrievePatient();
    void retrieveEHRs(Long patientId);
}
