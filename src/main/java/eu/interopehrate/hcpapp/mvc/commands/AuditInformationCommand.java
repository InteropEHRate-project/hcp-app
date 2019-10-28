package eu.interopehrate.hcpapp.mvc.commands;

import eu.interopehrate.hcpapp.jpa.entities.enums.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AuditInformationCommand<type> {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate eventDateTime;
    private EventType type;
    private String eventDescription;

    private List<AuditInformationCommand> auditInformationCommands;

    public AuditInformationCommand(List<AuditInformationCommand> auditInformationCommand) {
        this.auditInformationCommands=auditInformationCommand;
    }

    public AuditInformationCommand() {
    }
}
