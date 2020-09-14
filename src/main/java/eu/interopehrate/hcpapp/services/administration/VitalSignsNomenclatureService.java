package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.jpa.entities.VitalSignsTypesEntity;

import java.util.List;

public interface VitalSignsNomenclatureService {
    List<VitalSignsTypesEntity> getVitalSignsTypes();
    void addVitalSignsType(VitalSignsTypesEntity vitalSignsTypesEntity);
}
