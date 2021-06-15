package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandCurrentDisease implements Converter<CurrentDiseaseEntity, CurrentDiseaseInfoCommand> {

    public CurrentDiseaseInfoCommand convert(CurrentDiseaseEntity currentDiseaseEntity) {
        CurrentDiseaseInfoCommand currentDiseaseInfoCommand = new CurrentDiseaseInfoCommand();
        BeanUtils.copyProperties(currentDiseaseEntity, currentDiseaseInfoCommand);
        return currentDiseaseInfoCommand;
    }
}
