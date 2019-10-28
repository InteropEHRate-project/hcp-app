package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.mvc.commands.AuditInformationCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AuditInformationServiceImpl implements AuditInformationService {

    @Override
    public List<AuditInformationCommand> getAuditInformationCommand() {
        AuditInformationCommand auditInformationCommand = new AuditInformationCommand();
        auditInformationCommand.setDateTime(LocalDateTime.now());
        auditInformationCommand.setType(AuditEventType.OPEN_CONNECTION);
        auditInformationCommand.setDetails("ABC");
        return Collections.singletonList(auditInformationCommand);
    }
}
