package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandSample;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandLaboratoryTest implements Converter<LaboratoryTestsEntity, ObservationLaboratoryInfoCommandAnalysis> {

    @Override
    public ObservationLaboratoryInfoCommandAnalysis convert(LaboratoryTestsEntity laboratoryTestsEntity) {
        ObservationLaboratoryInfoCommandAnalysis laboratoryInfoCommandAnalysis = new ObservationLaboratoryInfoCommandAnalysis();
        laboratoryInfoCommandAnalysis.setId(laboratoryTestsEntity.getId());
        laboratoryInfoCommandAnalysis.setAuthor(laboratoryTestsEntity.getAuthor());
        laboratoryInfoCommandAnalysis.setAnalysisName(laboratoryTestsEntity.getLaboratoryTestsTypesEntity().getName());

        ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample = new ObservationLaboratoryInfoCommandSample();
        observationLaboratoryInfoCommandSample.setLocalDateOfLaboratory(laboratoryTestsEntity.getLocalDateOfLaboratory());
        observationLaboratoryInfoCommandSample.setCurrentValue(laboratoryTestsEntity.getCurrentValue());
        observationLaboratoryInfoCommandSample.setUnitOfMeasurement(laboratoryTestsEntity.getUnitOfMeasurement());
        laboratoryInfoCommandAnalysis.setObservationLaboratoryInfoCommandSample(observationLaboratoryInfoCommandSample);
        return laboratoryInfoCommandAnalysis;
    }
}