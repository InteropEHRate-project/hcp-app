package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription.PrescriptionController;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import org.springframework.stereotype.Service;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Override
    public PrescriptionEntity find(Long id) {
        for (PrescriptionEntity pr : PrescriptionController.prescriptionEntityList) {
            if (pr.getId().equals(id)) {
                return pr;
            }
        }
        return null;
    }
}
