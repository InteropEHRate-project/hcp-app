package eu.interopehrate.hcpapp.converters.entity;

import eu.interopehrate.hcpapp.jpa.entities.AuditInformationEntity;
import eu.interopehrate.hcpapp.mvc.commands.administration.AuditInformationCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandAuditInformation implements Converter<AuditInformationEntity, AuditInformationCommand> {
    @Override
    public AuditInformationCommand convert(AuditInformationEntity auditInformationEntity) {
        AuditInformationCommand auditInformationCommand = new AuditInformationCommand();
        auditInformationCommand.setDateTime(auditInformationEntity.getDateTime());
        auditInformationCommand.setType(auditInformationEntity.getType());
        auditInformationCommand.setDetails(auditInformationEntity.getDetails());
        return auditInformationCommand;
    }
}
