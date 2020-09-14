package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.jpa.entities.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsTypesRepository;
import eu.interopehrate.hcpapp.services.administration.VitalSignsNomenclatureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VitalSignsNomenclatureServiceImpl implements VitalSignsNomenclatureService {
    private final VitalSignsTypesRepository vitalSignsTypesRepository;

    public VitalSignsNomenclatureServiceImpl(VitalSignsTypesRepository vitalSignsTypesRepository) {
        this.vitalSignsTypesRepository = vitalSignsTypesRepository;
    }

    @Override
    public List<VitalSignsTypesEntity> getVitalSignsTypesEntity() {
        return this.vitalSignsTypesRepository.findAll();
    }

    @Override
    public void addVitalSignsTypesEntity(VitalSignsTypesEntity vitalSignsTypesEntity) {
        if (this.vitalSignsTypesRepository.findAll().contains(vitalSignsTypesEntity)) {
            return;
        }
        this.vitalSignsTypesRepository.save(vitalSignsTypesEntity);
    }
}
