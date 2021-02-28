package eu.interopehrate.hcpapp.services.currentpatient.currentmedications;


import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;

public interface MedicationService {
    CurrentPatient getCurrentPatient();
    PrescriptionEntity find(Long id);
}
