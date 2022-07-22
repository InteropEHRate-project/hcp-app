package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandReason;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.ReasonRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ReasonInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.ReasonService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReasonServiceImpl implements ReasonService {
    private final ReasonRepository reasonRepository;
    private final AuditInformationService auditInformationService;
    private final CurrentD2DConnection currentD2DConnection;
    private final CurrentPatient currentPatient;
    private final EntityToCommandReason entityToCommandReason;

    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;

    public ReasonServiceImpl(ReasonRepository reasonRepository, AuditInformationService auditInformationService, CurrentD2DConnection currentD2DConnection, CurrentPatient currentPatient, EntityToCommandReason entityToCommandReason) {
        this.reasonRepository = reasonRepository;
        this.auditInformationService = auditInformationService;
        this.currentD2DConnection = currentD2DConnection;
        this.currentPatient = currentPatient;
        this.entityToCommandReason = entityToCommandReason;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return this.currentD2DConnection;
    }

    @Override
    public List<ReasonEntity> getReasons() {
        var list = this.reasonRepository.findAll();
        Collections.sort(list);
        return list;
    }

    @Override
    public ReasonRepository getReasonRepository() {
        return reasonRepository;
    }

    @Override
    public void addSymptom(String symptom) {
        ReasonEntity reasonEntity = new ReasonEntity();
        reasonEntity.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " +
                healthCareProfessionalService.getHealthCareProfessional().getLastName());
        reasonEntity.setSymptom(symptom);
        if (this.reasonRepository.findAll().contains(symptom)) {
            return;
        }
        this.reasonRepository.save(reasonEntity);
    }

    @Override
    public Condition callReason() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.reasonRepository.findAll().size(); i++) {
                Condition vitalSigns = createReasonFromEntity(this.reasonRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                return vitalSigns;
            }
            auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send PH Examinations to S-EHR");
            this.reasonRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    private static Condition createReasonFromEntity(ReasonEntity reasonEntity) {
        Condition symptom = new Condition();

        symptom.setId("obs-reason-29299-5");
        symptom.setCode(new CodeableConcept());
        symptom.getCode().addChild("coding");
        symptom.getCode().setCoding(new ArrayList<>());
        symptom.getCode().getCoding().add(new Coding()
                .setSystem("http://loinc.org")
                .setCode("29299-5")
                .setDisplay("Reason for visit"));
        symptom.addNote().setText(reasonEntity.getSymptom());

        return symptom;
    }

    @Override
    public void delete(Long id) {
        this.reasonRepository.deleteById(id);
    }

    @Override
    public List<ReasonInfoCommand> getNewReason() {
        return this.reasonRepository.findAll()
                .stream()
                .map(this.entityToCommandReason::convert)
                .collect(Collectors.toList());
    }
}
