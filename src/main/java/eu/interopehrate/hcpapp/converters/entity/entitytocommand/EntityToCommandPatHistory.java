package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PatHistoryEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandPatHistory implements Converter<PatHistoryEntity, PatHistoryInfoCommandDiagnosis> {
    @Override
    public PatHistoryInfoCommandDiagnosis convert(PatHistoryEntity patHistoryEntity) {
        PatHistoryInfoCommandDiagnosis patHistoryInfoCommandDiagnosis = new PatHistoryInfoCommandDiagnosis();
        BeanUtils.copyProperties(patHistoryEntity, patHistoryInfoCommandDiagnosis);
        return patHistoryInfoCommandDiagnosis;
    }
}
