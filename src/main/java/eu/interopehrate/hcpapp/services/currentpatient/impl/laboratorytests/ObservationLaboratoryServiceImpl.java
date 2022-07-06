package eu.interopehrate.hcpapp.services.currentpatient.impl.laboratorytests;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityLaboratory;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandLaboratoryTest;
import eu.interopehrate.hcpapp.converters.fhir.laboratorytests.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsTypesEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;
    private boolean isFiltered = false;
    private boolean isEmpty = false;
    private final CloudConnection cloudConnection;
    private final CurrentD2DConnection currentD2DConnection;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    private final CommandToEntityLaboratory commandToEntityLaboratory;
    private final LaboratoryTestsRepository laboratoryTestsRepository;
    private final LaboratoryTestsTypesRepository laboratoryTestsTypesRepository;
    private final EntityToCommandLaboratoryTest entityToCommandLaboratoryTest;
    private final AuditInformationService auditInformationService;

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory, CloudConnection cloudConnection, CurrentD2DConnection currentD2DConnection, CommandToEntityLaboratory commandToEntityLaboratory, LaboratoryTestsRepository laboratoryTestsRepository, LaboratoryTestsTypesRepository laboratoryTestsTypesRepository, EntityToCommandLaboratoryTest entityToCommandLaboratoryTest, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;
        this.cloudConnection = cloudConnection;
        this.currentD2DConnection = currentD2DConnection;
        this.commandToEntityLaboratory = commandToEntityLaboratory;
        this.laboratoryTestsRepository = laboratoryTestsRepository;
        this.laboratoryTestsTypesRepository = laboratoryTestsTypesRepository;
        this.entityToCommandLaboratoryTest = entityToCommandLaboratoryTest;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public boolean isFiltered() {
        return isFiltered;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis(String keyword) {
        var observationLaboratoryInfoCommandAnalyses = currentPatient.laboratoryList()
                .stream()
                .filter(laboratory -> laboratory.hasCategory() && ("laboratory".equals(laboratory.getCategoryFirstRep().getCodingFirstRep().getCode())
                        || "RAD".equals(laboratory.getCategoryFirstRep().getCodingFirstRep().getCode())))
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());

        if (keyword == null || keyword.equals("empty") || keyword.trim().equals("")) {
            observationLaboratoryInfoCommandAnalyses.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
            this.isFiltered = false;
            this.isEmpty = false;
            return ObservationLaboratoryCommandAnalysis.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses)
                    .build();
        }

        if (Objects.nonNull(keyword) && !keyword.trim().equals("")) {
            //The filtration is happening...
            List<ObservationLaboratoryInfoCommandAnalysis> laboratoryInfoCommandList = new ArrayList<>();
            observationLaboratoryInfoCommandAnalyses.forEach((lab) -> {
                if (lab.getAnalysis().toLowerCase().contains(keyword.toLowerCase())) {
                    laboratoryInfoCommandList.add(lab);
                }
            });
            if (laboratoryInfoCommandList.isEmpty()) {
                this.isEmpty = true;
                this.isFiltered = false;
            } else {
                this.isFiltered = true;
                this.isEmpty = false;
            }
            laboratoryInfoCommandList.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
            return ObservationLaboratoryCommandAnalysis.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .observationLaboratoryInfoCommandAnalyses(laboratoryInfoCommandList)
                    .build();
        }
        observationLaboratoryInfoCommandAnalyses.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses)
                .build();
    }

    @Override
    public void refreshData() {
        this.cloudConnection.downloadLabResults();
    }

    @SneakyThrows
    @Override
    public void getLaboratoryTests() {
        this.currentD2DConnection.getLaboratoryTestsResource();
    }

    @Override
    public void insertLaboratory(ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis) {
        observationLaboratoryInfoCommandAnalysis.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());

        LaboratoryTestsEntity laboratoryTestsEntity = this.commandToEntityLaboratory.convert(observationLaboratoryInfoCommandAnalysis);
        this.laboratoryTestsRepository.save(Objects.requireNonNull(laboratoryTestsEntity));
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HashMap correlations() {
        HashMap<String, String> correlationUnitWithType = new HashMap<>();
        for (LaboratoryTestsTypesEntity entity : this.laboratoryTestsTypesRepository.findAll()) {
            correlationUnitWithType.put(entity.getName(), entity.getUcum());
        }
        return correlationUnitWithType;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis laboratoryUpload() {
        var laboratoryList = this.laboratoryTestsRepository.findAll()
                .stream()
                .map(this.entityToCommandLaboratoryTest::convert)
                .collect(Collectors.toList());
        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(laboratoryList)
                .build();
    }

    @Override
    public void deleteLaboratory(String an, String sample) {
        LocalDateTime localDateTime = LocalDateTime.parse(sample, DateTimeFormatter.ofPattern("M/d/yy, h:mm a", Locale.US));
        for (LaboratoryTestsEntity l : this.laboratoryTestsRepository.findAll()) {
            if (l.getLocalDateOfLaboratory().equals(localDateTime) && l.getLaboratoryTestsTypesEntity().getName().equalsIgnoreCase(an)) {
                this.laboratoryTestsRepository.delete(l);
            }
        }
    }

    @Override
    public Observation callLaboratoryTests() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.laboratoryTestsRepository.findAll().size(); i++) {
                Observation laboratory = createLaboratoryFromEntity(this.laboratoryTestsRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(laboratory));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(laboratory));
                return laboratory;
            }
            auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send Laboratory test to S-EHR");
            this.laboratoryTestsRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    private static Observation createLaboratoryFromEntity(LaboratoryTestsEntity laboratoryTestsEntity) {
        Observation laboratory = new Observation();

        laboratory.setCode(new CodeableConcept());
        laboratory.getCode().addChild("coding");
        laboratory.getCode().setCoding(new ArrayList<>());
        laboratory.getCode().getCoding().add(new Coding().setSystem("http://loinc.org").setCode(laboratoryTestsEntity.getLaboratoryTestsTypesEntity().getLoinc()));
        laboratory.getCode().getCoding().get(0).setDisplay(laboratoryTestsEntity.getLaboratoryTestsTypesEntity().getName());

        laboratory.setId(UUID.randomUUID().toString());
        laboratory.setCategory(Collections.singletonList(new CodeableConcept().addCoding(new Coding().setCode("laboratory"))));

        Calendar when = Calendar.getInstance();
        int y = laboratoryTestsEntity.getLocalDateOfLaboratory().getYear();
        int m = monthNumberLaboratory(laboratoryTestsEntity.getLocalDateOfLaboratory().getMonthValue());
        int d = laboratoryTestsEntity.getLocalDateOfLaboratory().getDayOfMonth();
        int h = laboratoryTestsEntity.getLocalDateOfLaboratory().getHour();
        int min = laboratoryTestsEntity.getLocalDateOfLaboratory().getMinute();
        when.set(y, m, d, h, min);
        laboratory.setEffective(new DateTimeType(when));

        Quantity quantity = new Quantity();
        quantity.setUnit(laboratoryTestsEntity.getUnitOfMeasurement())
                .setValue(laboratoryTestsEntity.getCurrentValue())
                .setSystem("http://unitsofmeasure.org")
                .setCode(laboratoryTestsEntity.getUnitOfMeasurement());
        laboratory.setValue(quantity);

        // laboratory.setPerformer(laboratoryTestsEntity.getAuthor());
        laboratory.setSubject(new Reference(laboratoryTestsEntity.getPatientId()));

        Extension provenance = laboratory.getExtensionByUrl("http://interopehrate.eu/fhir/StructureDefinition/ProvenanceExtension-IEHR");
        laboratory.addExtension().setValue(provenance);

        // TO do  add proper subject (Patient), encounter , performer()

        return laboratory;
    }

    //Special designed method that changes the number of month because in FHIR protocol JANUARY is represented by 0 value (the range is from 0 to 11).
    private static int monthNumberLaboratory(int monthNumberLaboratory) {
        switch (monthNumberLaboratory) {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return Calendar.FEBRUARY;
            case 3:
                return Calendar.MARCH;
            case 4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case 6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case 8:
                return Calendar.AUGUST;
            case 9:
                return Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            case 12:
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return currentD2DConnection;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationCommand() {
        var laboratoryInfoCommands = this.currentPatient.laboratoryList()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());

        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(laboratoryInfoCommands)
                .build();
    }
}
