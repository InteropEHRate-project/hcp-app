package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PatHistoryEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CommandToEntityPatHistory implements Converter<PatHistoryInfoCommandDiagnosis, PatHistoryEntity> {

    @Override
    public PatHistoryEntity convert(PatHistoryInfoCommandDiagnosis source) {
        PatHistoryEntity patHistoryEntity = new PatHistoryEntity();
        BeanUtils.copyProperties(source, patHistoryEntity);
        return patHistoryEntity;
    }
}
