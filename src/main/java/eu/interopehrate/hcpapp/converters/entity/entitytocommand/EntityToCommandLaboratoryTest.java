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
        BeanUtils.copyProperties(laboratoryTestsEntity, laboratoryInfoCommandAnalysis);

        ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample = new ObservationLaboratoryInfoCommandSample();
        BeanUtils.copyProperties(laboratoryTestsEntity, observationLaboratoryInfoCommandSample);
        laboratoryInfoCommandAnalysis.setObservationLaboratoryInfoCommandSample(observationLaboratoryInfoCommandSample);
        return laboratoryInfoCommandAnalysis;
    }
}