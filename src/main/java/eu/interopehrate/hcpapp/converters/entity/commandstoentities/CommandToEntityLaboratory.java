package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommandToEntityLaboratory implements Converter<ObservationLaboratoryInfoCommandAnalysis, LaboratoryTestsEntity> {
    private final LaboratoryTestsTypesRepository laboratoryTestsTypesRepository;

    public CommandToEntityLaboratory(LaboratoryTestsTypesRepository laboratoryTestsTypesRepository) {
        this.laboratoryTestsTypesRepository = laboratoryTestsTypesRepository;
    }

    @Override
    public LaboratoryTestsEntity convert(ObservationLaboratoryInfoCommandAnalysis source) {
        LaboratoryTestsEntity laboratoryTestsEntity = new LaboratoryTestsEntity();
        laboratoryTestsEntity.setAuthor(source.getAuthor());

        for (LaboratoryTestsTypesEntity vt : this.laboratoryTestsTypesRepository.findAll()) {
            if (vt.getName().equalsIgnoreCase(source.getAnalysisName())) {
                laboratoryTestsEntity.setLaboratoryTestsTypesEntity(vt);
                break;
            }
        }
        if (Objects.isNull(source.getAnalysisName())) {
            throw new IllegalArgumentException("Analysis type cannot be found in the HCP local Data Base");
        }
        laboratoryTestsEntity.setLocalDateOfLaboratory(source.getObservationLaboratoryInfoCommandSample().getLocalDateOfLaboratory());
        laboratoryTestsEntity.setCurrentValue(source.getObservationLaboratoryInfoCommandSample().getCurrentValue());
        laboratoryTestsEntity.setUnitOfMeasurement(source.getObservationLaboratoryInfoCommandSample().getUnitOfMeasurement());
        return laboratoryTestsEntity;
    }
}