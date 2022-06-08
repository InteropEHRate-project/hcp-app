package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityLaboratory implements Converter<ObservationLaboratoryInfoCommandAnalysis, LaboratoryTestsEntity> {

    @Override
    public LaboratoryTestsEntity convert(ObservationLaboratoryInfoCommandAnalysis source) {
        LaboratoryTestsEntity laboratoryTestsEntity = new LaboratoryTestsEntity();
        BeanUtils.copyProperties(source, laboratoryTestsEntity);
        return laboratoryTestsEntity;
    }
}