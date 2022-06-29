package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPHExam;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPHExam;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.PHExamEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PHExamServiceImpl implements PHExamService {
    private final CurrentPatient currentPatient;
    private final PHExamRepository phExamRepository;
    private final EntityToCommandPHExam entityToCommandPHExam;
    private final List<String> listOfClinicalExam = new ArrayList<>();
    private final CurrentD2DConnection currentD2DConnection;
    private final CommandToEntityPHExam commandToEntityPHExam;
    private final AuditInformationService auditInformationService;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;


    public PHExamServiceImpl(CurrentPatient currentPatient, PHExamRepository phExamRepository, EntityToCommandPHExam entityToCommandPHExam, CurrentD2DConnection currentD2DConnection, CommandToEntityPHExam commandToEntityPHExam, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.phExamRepository = phExamRepository;
        this.entityToCommandPHExam = entityToCommandPHExam;
        this.currentD2DConnection = currentD2DConnection;
        this.commandToEntityPHExam = commandToEntityPHExam;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public PHExamCommand phExamCommand() {
        return PHExamCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .listClinicalExam(this.listOfClinicalExam)
                .build();
    }

    @Override
    public void insertClinicalExam(String clinicalExam) {
        if (clinicalExam != null && !clinicalExam.trim().equals("") && !this.listOfClinicalExam.contains(clinicalExam)) {
            this.listOfClinicalExam.add(clinicalExam);
        }
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return this.currentD2DConnection;
    }

    @Override
    public PHExamRepository getPHExamRepository() {
        return phExamRepository;
    }

    @Override
    public List<PHExamInfoCommand> getNewPhExam() {
        return this.phExamRepository.findAll()
                .stream()
                .map(this.entityToCommandPHExam::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void insertPhExam(PHExamInfoCommand phExamInfoCommand) {
        phExamInfoCommand.setPatientId(this.currentPatient.getPatient().getId());
        phExamInfoCommand.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " "
                + healthCareProfessionalService.getHealthCareProfessional().getLastName());

        this.phExamRepository.save(Objects.requireNonNull(this.commandToEntityPHExam.convert(phExamInfoCommand)));
    }

    @Override
    public Condition callPHExam() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.phExamRepository.findAll().size(); i++) {
                Condition vitalSigns = createPHExamFromEntity(this.phExamRepository.findAll().get(i));
                this.currentPatient.getVitalSigns().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                this.currentPatient.getVitalSignsTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                return vitalSigns;
            }
            auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send PH Examinations to S-EHR");
            this.phExamRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    private static Condition createPHExamFromEntity(PHExamEntity phExamEntity) {
        Condition phExam = new Condition();

        phExam.setId(UUID.randomUUID().toString());
        phExam.setCode(new CodeableConcept());
        phExam.getCode().addChild("coding");
        phExam.getCode().setCoding(new ArrayList<>());
        phExam.getCode().getCoding().add(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v2-0074")
                .setCode("PHY"));
        phExam.getCode().getCoding().get(0).setDisplay(phExamEntity.getPhExam());

        phExam.addNote().setText(phExamEntity.getPhExam());

        return phExam;
    }

    @Override
    public void delete(String phNote) {
        this.listOfClinicalExam.removeIf(x -> x.equals(phNote));
        this.phExamRepository.deleteAll();
    }
}
