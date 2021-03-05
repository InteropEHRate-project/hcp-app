package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionTypesRepository;
import eu.interopehrate.hcpapp.services.administration.PrescriptionsNomenclatureService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PrescriptionsNomenclatureServiceImpl implements PrescriptionsNomenclatureService {
    private final PrescriptionTypesRepository prescriptionTypesRepository;

    public PrescriptionsNomenclatureServiceImpl(PrescriptionTypesRepository prescriptionTypesRepository) {
        this.prescriptionTypesRepository = prescriptionTypesRepository;
    }

    @Override
    public List<PrescriptionTypesEntity> getPrescriptionTypes() {
        var list = this.prescriptionTypesRepository.findAll();
        Collections.sort(list);
        return list;
    }

//    @Override
//    public void addVitalSignsType(VitalSignsTypesEntity vitalSignsTypesEntity) {
//        if (this.vitalSignsTypesRepository.findAll().contains(vitalSignsTypesEntity)) {
//            return;
//        }
//        this.vitalSignsTypesRepository.save(vitalSignsTypesEntity);
//    }
}
