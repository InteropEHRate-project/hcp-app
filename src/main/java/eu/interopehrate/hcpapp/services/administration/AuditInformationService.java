package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.jpa.entities.AuditInformationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;

import java.util.List;

public interface AuditInformationService {
    List<AuditInformationEntity> getAuditInformationCommand();

    void auditEvent(AuditEventType auditEventType, String details);

    void auditAdmissionData();

    void auditConsentData();
}
