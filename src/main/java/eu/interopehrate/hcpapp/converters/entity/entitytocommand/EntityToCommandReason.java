package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ReasonInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class EntityToCommandReason implements Converter<ReasonEntity, ReasonInfoCommand> {
    @Override
    public ReasonInfoCommand convert(ReasonEntity reasonEntity) {
        ReasonInfoCommand reasonInfoCommand = new ReasonInfoCommand();
        BeanUtils.copyProperties(reasonEntity, reasonInfoCommand);
        return reasonInfoCommand;
    }
}