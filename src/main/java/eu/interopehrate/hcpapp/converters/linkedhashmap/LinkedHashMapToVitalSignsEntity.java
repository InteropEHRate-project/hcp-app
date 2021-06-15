package eu.interopehrate.hcpapp.converters.linkedhashmap;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsTypesRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Objects;

@Component
@SuppressWarnings("rawtypes")
public class LinkedHashMapToVitalSignsEntity implements Converter<LinkedHashMap, VitalSignsEntity> {
    private final VitalSignsTypesRepository vitalSignsTypesRepository;

    public LinkedHashMapToVitalSignsEntity(VitalSignsTypesRepository vitalSignsTypesRepository) {
        this.vitalSignsTypesRepository = vitalSignsTypesRepository;
    }

    @Override
    public VitalSignsEntity convert(LinkedHashMap source) {
        VitalSignsEntity vitalSignsEntity = new VitalSignsEntity();
        if (Objects.nonNull(source.get("patient"))) {
            vitalSignsEntity.setPatientId(((LinkedHashMap) source.get("patient")).get("patientId").toString());
        }
        if (Objects.nonNull(source.get("author"))) {
            vitalSignsEntity.setAuthor(source.get("author").toString());
        }

        for (VitalSignsTypesEntity vt : this.vitalSignsTypesRepository.findAll()) {
            if (vt.getName().equalsIgnoreCase(((LinkedHashMap) source.get("analysisType")).get("name").toString())) {
                vitalSignsEntity.setAnalysisType(vt);
                break;
            }
        }
        if (Objects.isNull(vitalSignsEntity.getAnalysisType())) {
            throw new IllegalArgumentException("Analysis type cannot be found in the HCP local Data Base");
        }

        if (Objects.nonNull(source.get("localDateOfVitalSign"))) {
            vitalSignsEntity.setLocalDateOfVitalSign(LocalDateTime.parse(source.get("localDateOfVitalSign").toString()));
        }
        if (Objects.nonNull(source.get("currentValue"))) {
            vitalSignsEntity.setCurrentValue(Double.parseDouble(source.get("currentValue").toString()));
        }
        if (Objects.nonNull(source.get("unitOfMeasurement"))) {
            vitalSignsEntity.setUnitOfMeasurement(source.get("unitOfMeasurement").toString());
        }
        return vitalSignsEntity;
    }
}
