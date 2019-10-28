package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.AuditInformationCommand;

import java.util.List;

public interface AuditInformationService {
    List<AuditInformationCommand> getAuditInformationCommand();
}
