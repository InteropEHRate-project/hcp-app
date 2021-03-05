package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionTypesEntity;

import java.util.List;

public interface PrescriptionsNomenclatureService {
    List<PrescriptionTypesEntity> getPrescriptionTypes();
    void addPrescriptionType(PrescriptionTypesEntity prescriptionTypesEntity);
}
