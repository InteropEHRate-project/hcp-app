package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.jpa.entities.enums.EventType;
import eu.interopehrate.hcpapp.mvc.commands.AuditInformationCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

@Service
public class AuditInformationServiceImpl implements AuditInformationService {

    @Override
    public List<AuditInformationCommand> getAuditInformationCommand() {
        AuditInformationCommand auditInformationCommand = new AuditInformationCommand();
        auditInformationCommand.setEventDateTime(LocalDate.of(2018, Month.AUGUST, 22));
        auditInformationCommand.setType(EventType.OpenConnection);
        auditInformationCommand.setEventDescription("ABC");
        return Collections.singletonList(auditInformationCommand);
    }
}
