package eu.interopehrate.hcpapp.converters.linkedhashmap;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsTypesRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Objects;

@Component
@SuppressWarnings("rawtypes")
public class LinkedHashMapToLaboratoryEntity implements Converter<LinkedHashMap, LaboratoryTestsEntity> {
    private final LaboratoryTestsTypesRepository laboratoryTestsTypesRepository;

    public LinkedHashMapToLaboratoryEntity(LaboratoryTestsTypesRepository laboratoryTestsTypesRepository) {
        this.laboratoryTestsTypesRepository = laboratoryTestsTypesRepository;
    }

    @Override
    public LaboratoryTestsEntity convert(LinkedHashMap linkedHashMap) {
        LaboratoryTestsEntity laboratoryTestsEntity = new LaboratoryTestsEntity();

        if (Objects.nonNull(linkedHashMap.get("author"))) {
            laboratoryTestsEntity.setAuthor(linkedHashMap.get("author").toString());
        }

        for (LaboratoryTestsTypesEntity vt : this.laboratoryTestsTypesRepository.findAll()) {
            if (vt.getName().equalsIgnoreCase(((LinkedHashMap) linkedHashMap.get("laboratoryTestsTypesEntity")).get("name").toString())) {
                laboratoryTestsEntity.setLaboratoryTestsTypesEntity(vt);
                break;
            }
        }
        if (Objects.isNull(laboratoryTestsEntity.getLaboratoryTestsTypesEntity())) {
            throw new IllegalArgumentException("Analysis type cannot be found in the HCP local Data Base");
        }

        if (Objects.nonNull(linkedHashMap.get("localDateOfLaboratory"))) {
            laboratoryTestsEntity.setLocalDateOfLaboratory(LocalDateTime.parse(linkedHashMap.get("localDateOfLaboratory").toString()));
        }
        if (Objects.nonNull(linkedHashMap.get("currentValue"))) {
            laboratoryTestsEntity.setCurrentValue(Double.parseDouble(linkedHashMap.get("currentValue").toString()));
        }
        if (Objects.nonNull(linkedHashMap.get("unitOfMeasurement"))) {
            laboratoryTestsEntity.setUnitOfMeasurement(linkedHashMap.get("unitOfMeasurement").toString());
        }
        return laboratoryTestsEntity;
    }
}
