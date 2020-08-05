package eu.interopehrate.hcpapp.converters.entity;

import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.springframework.core.convert.converter.Converter;

public class EntityToVitalSigns implements Converter<VitalSignsInfoCommand, VitalSignsEntity> {
    @Override
    public VitalSignsEntity convert(VitalSignsInfoCommand source) {
        VitalSignsEntity vitalSignsEntity = new VitalSignsEntity();
        vitalSignsEntity.setAnalysisName(source.getAnalysisName());
        vitalSignsEntity.setCurrentValue(source.getVitalSignsInfoCommandSample().getCurrentValue());
        vitalSignsEntity.setLocalDateOfVitalSign(source.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign());
        vitalSignsEntity.setUnitOfMeasurement(source.getVitalSignsInfoCommandSample().getUnitOfMeasurement());
        return vitalSignsEntity;
    }
}
