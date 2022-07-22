package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ReasonInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityReason implements Converter<ReasonInfoCommand, ReasonEntity> {

    @Override
    public ReasonEntity convert(ReasonInfoCommand source) {
        ReasonEntity reasonEntity = new ReasonEntity();
        BeanUtils.copyProperties(source, reasonEntity);
        return reasonEntity;
    }
}