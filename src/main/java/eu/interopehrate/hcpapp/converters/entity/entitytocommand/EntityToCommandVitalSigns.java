package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommandSample;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandVitalSigns implements Converter<VitalSignsEntity, VitalSignsInfoCommand> {

    @Override
    public VitalSignsInfoCommand convert(VitalSignsEntity vitalSignsEntity) {
        VitalSignsInfoCommand vitalSignsInfoCommand = new VitalSignsInfoCommand();
        vitalSignsInfoCommand.setId(vitalSignsEntity.getId());
        vitalSignsInfoCommand.setPatientId(vitalSignsEntity.getPatientId());
        vitalSignsInfoCommand.setAuthor(vitalSignsEntity.getAuthor());
        vitalSignsInfoCommand.setAnalysisName(vitalSignsEntity.getAnalysisType().getName());

        VitalSignsInfoCommandSample vitalSignsInfoCommandSample = new VitalSignsInfoCommandSample();
        BeanUtils.copyProperties(vitalSignsEntity, vitalSignsInfoCommandSample);
        vitalSignsInfoCommand.setVitalSignsInfoCommandSample(vitalSignsInfoCommandSample);
        return vitalSignsInfoCommand;
    }
}
