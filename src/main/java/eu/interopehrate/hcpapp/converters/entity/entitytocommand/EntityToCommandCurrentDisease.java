package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandCurrentDisease implements Converter<CurrentDiseaseEntity, CurrentDiseaseInfoCommand> {

    public CurrentDiseaseInfoCommand convert(CurrentDiseaseEntity currentDiseaseEntity) {
        CurrentDiseaseInfoCommand currentDiseaseInfoCommand = new CurrentDiseaseInfoCommand();
        currentDiseaseInfoCommand.setId(currentDiseaseEntity.getId());
        currentDiseaseInfoCommand.setDisease(currentDiseaseEntity.getDisease());
        currentDiseaseInfoCommand.setDateOfDiagnosis(currentDiseaseEntity.getDateOfDiagnosis());
        currentDiseaseInfoCommand.setComment(currentDiseaseEntity.getComment());
        return currentDiseaseInfoCommand;
    }
}
