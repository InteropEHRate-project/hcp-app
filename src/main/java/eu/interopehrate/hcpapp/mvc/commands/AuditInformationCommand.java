package eu.interopehrate.hcpapp.mvc.commands;

import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditInformationCommand {
    private LocalDateTime dateTime;
    private AuditEventType type;
    private String details;
}
