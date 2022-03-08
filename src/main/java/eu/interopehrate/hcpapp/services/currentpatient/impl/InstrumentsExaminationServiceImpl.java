package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityInstrumentsExam;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandInstrumentsExam;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.InstrumentsExaminationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.InstrumentsExaminationRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.currentpatient.InstrumentsExaminationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstrumentsExaminationServiceImpl implements InstrumentsExaminationService {
    private final CurrentPatient currentPatient;
    private final InstrumentsExaminationRepository instrumentsExaminationRepository;
    private final EntityToCommandInstrumentsExam entityToCommandInstrumentsExam;
    private final CommandToEntityInstrumentsExam commandToEntityInstrumentsExam;
    private final CurrentD2DConnection currentD2DConnection;
    private final AuditInformationService auditInformationService;

    public InstrumentsExaminationServiceImpl(CurrentPatient currentPatient, InstrumentsExaminationRepository instrumentsExaminationRepository, EntityToCommandInstrumentsExam entityToCommandInstrumentsExam, CommandToEntityInstrumentsExam commandToEntityInstrumentsExam, CurrentD2DConnection currentD2DConnection, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.instrumentsExaminationRepository = instrumentsExaminationRepository;
        this.entityToCommandInstrumentsExam = entityToCommandInstrumentsExam;
        this.commandToEntityInstrumentsExam = commandToEntityInstrumentsExam;
        this.currentD2DConnection = currentD2DConnection;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public InstrumentsExaminationCommand instrExam() {
        var listOfInstrExams = this.instrumentsExaminationRepository.findAll()
                .stream()
                .map(this.entityToCommandInstrumentsExam::convert)
                .collect(Collectors.toList());
        return InstrumentsExaminationCommand.builder()
                .instrumentsExaminationInfoCommandsExamInfoCommands(listOfInstrExams)
                .build();
    }

    @Override
    public void insertInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand) {
        this.instrumentsExaminationRepository.save(this.commandToEntityInstrumentsExam.convert(instrumentsExaminationInfoCommand));
    }

    @Override
    public InstrumentsExaminationInfoCommand retrieveInstrExamById(Long id) {
        return this.entityToCommandInstrumentsExam.convert(this.instrumentsExaminationRepository.getOne(id));
    }

    @Override
    public void updateInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand) {
        InstrumentsExaminationEntity instrumentsExaminationEntity = this.instrumentsExaminationRepository.getOne(instrumentsExaminationInfoCommand.getId());
        BeanUtils.copyProperties(instrumentsExaminationInfoCommand, instrumentsExaminationEntity);
        this.instrumentsExaminationRepository.save(instrumentsExaminationEntity);
    }

    @Override
    public void deleteInstrExam(Long id) {
        this.instrumentsExaminationRepository.deleteById(id);
    }

    @Override
    public List<InstrumentsExaminationInfoCommand> listNewInstrumentExamination() {
        return this.instrumentsExaminationRepository.findAll()
                .stream()
                .map(this.entityToCommandInstrumentsExam::convert)
                .collect(Collectors.toList());
    }

    @Override
    public InstrumentsExaminationRepository getInstrumentsExaminationRepository() {
        return instrumentsExaminationRepository;
    }

    @Override
    public void callSendInstrumentalExamination() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            Bundle examination = new Bundle();
            examination.setEntry(new ArrayList<>());
            for (int i = 0; i < this.instrumentsExaminationRepository.findAll().size(); i++) {
                examination.getEntry().add(new Bundle.BundleEntryComponent());
                DocumentReference med = createInstrumentalExaminationFromEntity(this.instrumentsExaminationRepository.findAll().get(i));
                examination.getEntry().get(i).setResource(med);
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(med));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(med));
            }
            this.sendInstrumentalExamination(examination);
        } else {
            log.error("The connection with S-EHR is not established.");
        }
    }

    @Override
    @SneakyThrows
    public void sendInstrumentalExamination(Bundle instrExamination) {
        this.currentD2DConnection.getTd2D().sendHealthData(instrExamination);
        log.info("Instruments Examination sent to S-EHR");
        auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send Instruments Examination to S-EHR");
        this.instrumentsExaminationRepository.deleteAll();
    }

    private static DocumentReference createInstrumentalExaminationFromEntity(InstrumentsExaminationEntity instrumentsExaminationEntity) {
        DocumentReference documentReference = new DocumentReference();

        documentReference.getContent().add(new DocumentReference.DocumentReferenceContentComponent());
        documentReference.setType(new CodeableConcept().addCoding(new Coding().setSystem("https://loinc.org").setCode("29545-1")));
        documentReference.getContentFirstRep().getAttachment().setContentType("application/pdf");
        documentReference.getContentFirstRep().getAttachment().setData(instrumentsExaminationEntity.getData());
        documentReference.getContentFirstRep().getAttachment().setTitle("Instrumental Examination");
        documentReference.getContentFirstRep().getAttachment().setCreationElement(DateTimeType.now());

//        DiagnosticReport diagnosticReport = new DiagnosticReport();
//        diagnosticReport.setConclusion(instrumentsExaminationEntity.getResult());
//        Date dateStart = Date.from(instrumentsExaminationEntity.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
//        diagnosticReport.setIssued(dateStart);
//        List<Reference> r = new ArrayList<>();
//        r.add(new Reference());
//        r.get(0).setReference(instrumentsExaminationEntity.getAuthor());
//        return diagnosticReport;

        return documentReference;
    }

    @SneakyThrows
    @Override
    public InstrumentsExaminationEntity store(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        InstrumentsExaminationEntity fileDB = new InstrumentsExaminationEntity(fileName, file.getContentType(), file.getBytes());
        return instrumentsExaminationRepository.save(fileDB);
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return this.currentD2DConnection;
    }

    @Override
    public List<InstrumentsExaminationEntity> getFiles() {
        return instrumentsExaminationRepository.findAll();
    }
}
