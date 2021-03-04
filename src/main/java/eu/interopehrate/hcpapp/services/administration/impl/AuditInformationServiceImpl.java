package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.AuditInformationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.AuditInformationRepository;
import eu.interopehrate.hcpapp.services.administration.AdmissionDataAuditService;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AuditInformationServiceImpl implements AuditInformationService {
    private final AuditInformationRepository auditInformationRepository;
    private final AdmissionDataAuditService admissionDataAuditService;
    private final CurrentPatient currentPatient;

    public AuditInformationServiceImpl(AuditInformationRepository auditInformationRepository,
                                       AdmissionDataAuditService admissionDataAuditService,
                                       CurrentPatient currentPatient) {
        this.auditInformationRepository = auditInformationRepository;
        this.admissionDataAuditService = admissionDataAuditService;
        this.currentPatient = currentPatient;
    }

    @Override
    public List<AuditInformationEntity> getAuditInformationCommand() {
        log.info("Auditing");
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

    @Override
    public void auditConsentData() {
        Patient patient = this.currentPatient.getPatient();
        if (Objects.nonNull(patient)) {
            HumanName patientName = patient.getName().get(0);
            String patientInfo = String.join(", ", patientName.getGivenAsSingleString(), patientName.getFamily(), patient.getId());
            String auditDetails = String.join("->", patientInfo, this.currentPatient.getConsentAsString());
            this.auditEvent(AuditEventType.AUDIT_CONSENT, auditDetails);
        }

    }

    @Override
    public void auditEmergencyGetIps() {
        Patient patient = this.currentPatient.getPatient();
        if (Objects.nonNull(patient)) {
            HumanName patientName = patient.getName().get(0);
            String patientInfo = String.join(", ", patientName.getGivenAsSingleString(), patientName.getFamily(), patient.getId());
            this.auditEvent(AuditEventType.EMERGENCY_GET_IPS, patientInfo);
        }
    }
}
