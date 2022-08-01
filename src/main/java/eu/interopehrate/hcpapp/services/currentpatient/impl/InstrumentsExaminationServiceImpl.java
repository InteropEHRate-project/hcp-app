package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.dicom_anonymization_library.DICOMAnonymization;
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
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.InstrumentsExaminationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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
    private final List<String> listOfResultNote = new ArrayList<>();
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;

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
                .listOfResultNote(this.listOfResultNote)
                .build();
    }

    @Override
    public void insertInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand) {
        instrumentsExaminationInfoCommand.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() +
                " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        this.instrumentsExaminationRepository.save(Objects.requireNonNull(this.commandToEntityInstrumentsExam.convert(instrumentsExaminationInfoCommand)));
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
    public DiagnosticReport callSendInstrumentalExamination() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.instrumentsExaminationRepository.findAll().size(); i++) {
                DiagnosticReport med = createInstrumentalExaminationFromEntity1(this.instrumentsExaminationRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(med));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(med));
                return med;
            }
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    public Media callSendInstrumentalExaminationMedia() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.instrumentsExaminationRepository.findAll().size(); i++) {
                Media media = createMediaFromEntity(this.instrumentsExaminationRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(media));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(media));
                return media;
            }
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    public Media callSendInstrumentalExaminationMediaAnon() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.instrumentsExaminationRepository.findAll().size(); i++) {
                Media media = createMediaAnonFromEntity(this.instrumentsExaminationRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(media));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(media));
                return media;
            }
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void sendInstrumentalExamination(Bundle instrExamination) {
        this.currentD2DConnection.getTd2D().sendHealthData(instrExamination);
        log.info("Instruments Examination sent to S-EHR");
        auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send Instruments Examination to S-EHR");
        this.instrumentsExaminationRepository.deleteAll();
    }

    private static Media createMediaFromEntity(InstrumentsExaminationEntity instrumentsExaminationEntity) {
        Media imageNonAnon = new Media();

        Meta profileMedia = new Meta();
        profileMedia.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Media-IEHR");
        imageNonAnon.setMeta(profileMedia);

        imageNonAnon.setId(instrumentsExaminationEntity.getType().toString());
        imageNonAnon.setOperator(new Reference(instrumentsExaminationEntity.getAuthor()));
        imageNonAnon.setStatus(Media.MediaStatus.COMPLETED);
        imageNonAnon.getContent().setData(instrumentsExaminationEntity.getData())
                .setContentType("application/dicom")
                .setSize(instrumentsExaminationEntity.getData().length);
        imageNonAnon.addNote().setText(instrumentsExaminationEntity.getResultNote());

        return imageNonAnon;
    }

    private static Media createMediaAnonFromEntity(InstrumentsExaminationEntity instrumentsExaminationEntity) {
        Media imageNonAnon = new Media();
        imageNonAnon.getContent().setData(Base64.getEncoder().encode(instrumentsExaminationEntity.getData()))
                .setContentType("application/dicom+zip")
                .setSize(instrumentsExaminationEntity.getData().length);

        Media imageAnon = new Media();
        Meta profileMedia = new Meta();
        profileMedia.addProfile("http://interopehrate.eu/fhir/StructureDefinition/AnonymizationExtension-IEHR");
        imageAnon.setMeta(profileMedia);

        imageAnon.setId("anonymized");
        imageAnon.setStatus(Media.MediaStatus.COMPLETED);
        imageAnon.setOperator(new Reference(instrumentsExaminationEntity.getAuthor()));
        imageAnon.addExtension().setUrl("http://interopehrate.eu/fhir/StructureDefinition/AnonymizationExtension-IEHR")
                .setValue(new CodeableConcept().addCoding()
                        .setSystem("http://interopehrate.eu/fhir/CodeSystem/AnonymizationType-IEHR")
                        .setCode("anonymization")
                        .setDisplay("Anonymization"));

        String base64Str = Base64.getEncoder().encodeToString(instrumentsExaminationEntity.getData());
        String baseUrlFtgm = "http://10.97.32.223:9000";
//        String baseUrlChu = "http://139.165.99.12:9000";
        try {
            DICOMAnonymization dicomAnonymizationFtgm = new DICOMAnonymization(baseUrlFtgm);
            String response = dicomAnonymizationFtgm.request(base64Str);
            imageAnon.getContent().setData(response.getBytes());

//          DICOMAnonymization dicomAnonymizationChu = new DICOMAnonymization(baseUrlChu);
//          String response = dicomAnonymizationChu.request(base64Str);
//          imageAnon.getContent().setData(response.getBytes());
        } catch (Exception e) {
            System.out.println("The port for anon it's not present");
        }

        return imageAnon;
    }

    private static DiagnosticReport createInstrumentalExaminationFromEntity1(InstrumentsExaminationEntity instrumentsExaminationEntity) {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setId(UUID.randomUUID().toString());

        if (instrumentsExaminationEntity.getType().toString().equals("ECHO")) {
            diagnosticReport.setCode(new CodeableConcept()
                    .setCoding(new ArrayList<>())
                    .addCoding(new Coding()
                            .setSystem("http://hl7.org/fhir/sid/icd-10")
                            .setCode("B246ZZZ")
                            .setDisplay(instrumentsExaminationEntity.getType().toString())));
        } else if (instrumentsExaminationEntity.getType().toString().equals("ECG")) {
            diagnosticReport.setCode(new CodeableConcept()
                    .setCoding(new ArrayList<>())
                    .addCoding(new Coding()
                            .setSystem("http://hl7.org/fhir/sid/icd-10")
                            .setCode("4A02X4Z")
                            .setDisplay(instrumentsExaminationEntity.getType().toString())));
        }

        diagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
        Meta profile = new Meta();
        profile.addProfile("http://interopehrate.eu/fhir/StructureDefinition/DiagnosticReport");
        diagnosticReport.setMeta(profile);

        //IMAGE
        Media imageNonAnon = new Media();
        Meta profileMedia = new Meta();
        profileMedia.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Media-IEHR");
        imageNonAnon.setMeta(profileMedia);
        imageNonAnon.setId(instrumentsExaminationEntity.getType().toString());
        imageNonAnon.setOperator(new Reference(instrumentsExaminationEntity.getAuthor()));
        imageNonAnon.setStatus(Media.MediaStatus.COMPLETED);
        imageNonAnon.getContent().setData(instrumentsExaminationEntity.getData())
                .setContentType("application/dicom+zip")
                .setSize(instrumentsExaminationEntity.getData().length);
        imageNonAnon.addNote().setText(instrumentsExaminationEntity.getResultNote());

        //IMAGE ANON
        Media imageAnon = new Media();
        Meta profileMediaAnon = new Meta();
        profileMedia.addProfile("http://interopehrate.eu/fhir/StructureDefinition/AnonymizationExtension-IEHR");
        imageAnon.setMeta(profileMediaAnon);

        imageAnon.setStatus(Media.MediaStatus.COMPLETED);
        imageAnon.setOperator(new Reference(instrumentsExaminationEntity.getAuthor()));
        imageAnon.addExtension().setUrl("http://interopehrate.eu/fhir/StructureDefinition/AnonymizationExtension-IEHR")
                .setValue(new CodeableConcept().addCoding()
                        .setDisplay("http://interopehrate.eu/fhir/CodeSystem/AnonymizationType-IEHR")
                        .setCode("anonymization")
                        .setDisplay("Anonymization"));

        String base64Str = Base64.getEncoder().encodeToString(instrumentsExaminationEntity.getData());
        String baseUrlFtgm = "http://10.97.32.223:9000";
        // String baseUrlChu = "http://139.165.99.12:9000";
        try {
            DICOMAnonymization dicomAnonymizationFtgm = new DICOMAnonymization(baseUrlFtgm);
            String response = dicomAnonymizationFtgm.request(base64Str);
            imageAnon.getContent().setData(response.getBytes());

//          DICOMAnonymization dicomAnonymizationChu = new DICOMAnonymization(baseUrlChu);
//          String response = dicomAnonymizationChu.request(base64Str);
//          imageAnon.getContent().setData(response.getBytes());
        } catch (Exception e) {
            System.out.println("The port for anon it's not present");
        }

        try {
            diagnosticReport.addMedia().setLink(new Reference(imageNonAnon)).getLink().setReference(createMediaFromEntity(instrumentsExaminationEntity).getId());
            diagnosticReport.addMedia().setLink(new Reference(imageAnon)).getLink().setReference(createMediaAnonFromEntity(instrumentsExaminationEntity).getId());
        } catch (NullPointerException e) {
            System.out.println("Media without reference.");
        }

        return diagnosticReport;
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

    @Override
    public void insertResultNote(String resultNote) {
        if (resultNote != null && !resultNote.trim().equals("") && !this.listOfResultNote.contains(resultNote)) {
            this.listOfResultNote.add(resultNote);
        }
    }
}
