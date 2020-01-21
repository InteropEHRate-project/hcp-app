package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.jpa.entities.AuditInformationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.AuditInformationRepository;
import eu.interopehrate.hcpapp.services.administration.AdmissionDataAuditService;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditInformationServiceImpl implements AuditInformationService {

    private final AuditInformationRepository auditInformationRepository;
    private final AdmissionDataAuditService admissionDataAuditService;

    public AuditInformationServiceImpl(AuditInformationRepository auditInformationRepository,
                                       AdmissionDataAuditService admissionDataAuditService) {
        this.auditInformationRepository = auditInformationRepository;
        this.admissionDataAuditService = admissionDataAuditService;
    }

    @Override
    public List<AuditInformationEntity> getAuditInformationCommand() {
        return auditInformationRepository.findAll(Sort.sort(AuditInformationEntity.class).by(AuditInformationEntity::getDateTime).descending());
    }

    @Override
    public void auditEvent(AuditEventType auditEventType, String details) {
        AuditInformationEntity auditInformationEntity = new AuditInformationEntity();
        auditInformationEntity.setDateTime(LocalDateTime.now());
        auditInformationEntity.setType(auditEventType);
        auditInformationEntity.setDetails(details);

        auditInformationRepository.save(auditInformationEntity);
    }

    @Override
    public void auditAdmissionData() {
        admissionDataAuditService.saveAdmissionData();
    }
}
