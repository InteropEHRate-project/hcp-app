package eu.interopehrate.hcpapp.services.currentpatient.currentmedications;


import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;

public interface MedicationService {
    PrescriptionEntity find(Long id);
}
