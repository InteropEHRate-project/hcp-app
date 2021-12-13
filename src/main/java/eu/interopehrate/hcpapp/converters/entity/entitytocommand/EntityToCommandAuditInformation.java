package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.administration.AuditInformationEntity;
import eu.interopehrate.hcpapp.mvc.commands.administration.AuditInformationCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandAuditInformation implements Converter<AuditInformationEntity, AuditInformationCommand> {
    @Override
    public AuditInformationCommand convert(AuditInformationEntity auditInformationEntity) {
        AuditInformationCommand auditInformationCommand = new AuditInformationCommand();
        BeanUtils.copyProperties(auditInformationEntity, auditInformationCommand);
        return auditInformationCommand;
    }
}
