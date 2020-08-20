package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityVitalSigns implements Converter<VitalSignsInfoCommand, VitalSignsEntity> {
    @Override
    public VitalSignsEntity convert(VitalSignsInfoCommand source) {
        VitalSignsEntity vitalSignsEntity = new VitalSignsEntity();
        vitalSignsEntity.setAnalysisName(source.getAnalysisName());
        vitalSignsEntity.setLocalDateOfVitalSign(source.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign());
        vitalSignsEntity.setCurrentValue(source.getVitalSignsInfoCommandSample().getCurrentValue());
        vitalSignsEntity.setUnitOfMeasurement(source.getVitalSignsInfoCommandSample().getUnitOfMeasurement());
        return vitalSignsEntity;
    }
}
