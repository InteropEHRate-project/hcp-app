package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityCurrentDisease implements Converter<CurrentDiseaseInfoCommand, CurrentDiseaseEntity> {

    @Override
    public CurrentDiseaseEntity convert(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        CurrentDiseaseEntity currentDiseaseEntity = new CurrentDiseaseEntity();
        BeanUtils.copyProperties(currentDiseaseInfoCommand, currentDiseaseEntity);
        return currentDiseaseEntity;
    }
}
