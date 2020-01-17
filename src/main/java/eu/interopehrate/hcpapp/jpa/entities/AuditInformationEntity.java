package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AUDIT_INFORMATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class AuditInformationEntity extends HCPApplicationEntity {
    @NotNull
    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;
    @NonNull
    @Enumerated(EnumType.STRING)
    private AuditEventType type;
    @NotNull
    @Column(name = "Details", length = 1000)
    private String details;
}
