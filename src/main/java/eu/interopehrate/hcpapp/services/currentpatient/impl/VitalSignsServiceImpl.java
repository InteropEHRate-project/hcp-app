package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityVitalSigns;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandVitalSigns;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandVitalSigns;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VitalSignsServiceImpl implements VitalSignsService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandVitalSigns hapiToCommandVitalSigns;
    private final VitalSignsRepository vitalSignsRepository;
    private final CommandToEntityVitalSigns commandToEntityVitalSigns;
    private final EntityToCommandVitalSigns entityToCommandVitalSigns;
    private final CurrentD2DConnection currentD2DConnection;
    private final VitalSignsTypesRepository vitalSignsTypesRepository;
    private final AuditInformationService auditInformationService;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public VitalSignsServiceImpl(CurrentPatient currentPatient, HapiToCommandVitalSigns hapiToCommandVitalSigns, VitalSignsRepository vitalSignsRepository,
                                 CommandToEntityVitalSigns commandToEntityVitalSigns, EntityToCommandVitalSigns entityToCommandVitalSigns,
                                 CurrentD2DConnection currentD2DConnection, VitalSignsTypesRepository vitalSignsTypesRepository,
                                 AuditInformationService auditInformationService, HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.currentPatient = currentPatient;
        this.hapiToCommandVitalSigns = hapiToCommandVitalSigns;
        this.vitalSignsRepository = vitalSignsRepository;
        this.commandToEntityVitalSigns = commandToEntityVitalSigns;
        this.entityToCommandVitalSigns = entityToCommandVitalSigns;
        this.currentD2DConnection = currentD2DConnection;
        this.vitalSignsTypesRepository = vitalSignsTypesRepository;
        this.auditInformationService = auditInformationService;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return currentD2DConnection;
    }

    @Override
    public VitalSignsCommand vitalSignsCommand() {

        ArrayList<String> listObs = new ArrayList<>();
        listObs.add("3097-3");listObs.add("789-8");listObs.add("75940-7");
        listObs.add("4544-3");listObs.add("2947-0");listObs.add("777-3");
        listObs.add("67151-1");listObs.add("33762-6");listObs.add("41995-2");
        listObs.add("2345-7");listObs.add("41995-2");listObs.add("2345-7");
        listObs.add("20977-5");listObs.add("77147-7");listObs.add("2069-3");
        listObs.add("2915-7");listObs.add("29430-6");listObs.add("2350-7");
        listObs.add("2276-4");listObs.add("2160-0");listObs.add("10230-1");
        listObs.add("2324-2");listObs.add("1742-6");listObs.add("718-7");
        listObs.add("26449-9");listObs.add("10230-1");listObs.add("29430-6");
        listObs.add("2823-3"); listObs.add("2951-2");listObs.add("20570-8");
        listObs.add("3084-1");listObs.add("1988-5");listObs.add("3091-6");

        var vitalSignsInfoCommands = this.currentPatient.vitalSignsList()
                .stream()
                .filter(vitalSigns -> (vitalSigns.hasId() && (vitalSigns.getId().contains("vitalsign") || vitalSigns.getId().contains("vital-sign"))) ||
                        (vitalSigns.hasCode() && !listObs.contains(vitalSigns.getCode().getCodingFirstRep().getCode())))
                .map(hapiToCommandVitalSigns::convert)
                .collect(Collectors.toList());

        return VitalSignsCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .vitalSignsInfoCommands(vitalSignsInfoCommands)
                .build();
    }

    @Override
    public void insertVitalSigns(VitalSignsInfoCommand vitalSignsInfoCommand) {
        vitalSignsInfoCommand.setPatientId(this.currentPatient.getPatient().getId());
        vitalSignsInfoCommand.setAuthor(this.healthCareProfessionalRepository.findAll().get(0).getFirstName() + " " + this.healthCareProfessionalRepository.findAll().get(0).getLastName());
        VitalSignsEntity vitalSignsEntity = this.commandToEntityVitalSigns.convert(vitalSignsInfoCommand);
        vitalSignsRepository.save(Objects.requireNonNull(vitalSignsEntity));
    }

    @Override
    public void deleteVitalSign(String an, String sample) {
        LocalDateTime localDateTime = LocalDateTime.parse(sample, DateTimeFormatter.ofPattern("M/d/yy, h:mm a", Locale.US));
        for (VitalSignsEntity v : this.vitalSignsRepository.findAll()) {
            if (v.getLocalDateOfVitalSign().equals(localDateTime) && v.getAnalysisType().getName().equalsIgnoreCase(an)) {
                this.vitalSignsRepository.delete(v);
            }
        }
    }

    @Override
    public VitalSignsInfoCommand getVitalSign(String an, String sample) {
        LocalDateTime localDateTime = LocalDateTime.parse(sample, DateTimeFormatter.ofPattern("M/d/yy, h:mm a", Locale.US));
        for (VitalSignsEntity v : this.vitalSignsRepository.findAll()) {
            if (v.getLocalDateOfVitalSign().equals(localDateTime) && v.getAnalysisType().getName().equalsIgnoreCase(an)) {
                return this.entityToCommandVitalSigns.convert(v);
            }
        }
        return null;
    }

    @Override
    public void editVitalSign(VitalSignsInfoCommand newVitalSign, VitalSignsInfoCommand oldVitalSign) {
        newVitalSign.setPatientId(this.currentPatient.getPatient().getId());
        newVitalSign.setId(this.idByAnAndSample(oldVitalSign.getAnalysisName(), oldVitalSign.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign()));
        VitalSignsEntity oldVitalSignsEntity = this.vitalSignsRepository.getOne(newVitalSign.getId());
        VitalSignsEntity temporaryVitalSignsEntity = this.commandToEntityVitalSigns.convert(newVitalSign);
        oldVitalSignsEntity.setLocalDateOfVitalSign(temporaryVitalSignsEntity.getLocalDateOfVitalSign());
        oldVitalSignsEntity.setUnitOfMeasurement(temporaryVitalSignsEntity.getUnitOfMeasurement());
        oldVitalSignsEntity.setCurrentValue(temporaryVitalSignsEntity.getCurrentValue());
        oldVitalSignsEntity.setAnalysisType(temporaryVitalSignsEntity.getAnalysisType());
        this.vitalSignsRepository.save(oldVitalSignsEntity);
    }

    @Override
    public VitalSignsCommand vitalSignsUpload() {
        var vitalSignsList = this.vitalSignsRepository.findAll()
                .stream()
                .map(this.entityToCommandVitalSigns::convert)
                .collect(Collectors.toList());
        return VitalSignsCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .vitalSignsInfoCommands(vitalSignsList)
                .build();
    }

    @Override
    public Observation callVitalSigns() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.vitalSignsRepository.findAll().size(); i++) {
                Observation vitalSigns = createVitalSignsFromEntity(this.vitalSignsRepository.findAll().get(i));
                this.currentPatient.getVitalSigns().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                this.currentPatient.getVitalSignsTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                return vitalSigns;
            }
            auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send VitalSigns to S-EHR");
            this.vitalSignsRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @SneakyThrows
    @Override
    public void sendVitalSigns(Bundle vitalSigns) {
        this.currentD2DConnection.getTd2D().sendHealthData(vitalSigns);
        log.info("VitalSigns sent to S-EHR");
        auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send VitalSigns to S-EHR");
        this.vitalSignsRepository.deleteAll();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HashMap correlations() {
        HashMap<String, String> correlationUnitWithType = new HashMap<>();
        for (VitalSignsTypesEntity entity : this.vitalSignsTypesRepository.findAll()) {
            correlationUnitWithType.put(entity.getName(), entity.getUcum());
        }
        return correlationUnitWithType;
    }

    private static Observation createVitalSignsFromEntity(VitalSignsEntity vitalSignsEntity) {
        Observation vitalSigns = new Observation();

        vitalSigns.setCode(new CodeableConcept());
        vitalSigns.getCode().addChild("coding");
        vitalSigns.getCode().setCoding(new ArrayList<>());
        vitalSigns.getCode().getCoding().add(new Coding().setSystem("http://loinc.org").setCode(vitalSignsEntity.getAnalysisType().getLoinc()));
        vitalSigns.getCode().getCoding().get(0).setDisplay(vitalSignsEntity.getAnalysisType().getName());

        vitalSigns.setId(UUID.randomUUID() + "-vitalsign");
        vitalSigns.setCategory(Collections.singletonList(new CodeableConcept().addCoding(new Coding().setCode("vitalsign"))));

        Calendar when = Calendar.getInstance();
        int y = vitalSignsEntity.getLocalDateOfVitalSign().getYear();
        int m = monthNumber(vitalSignsEntity.getLocalDateOfVitalSign().getMonthValue());
        int d = vitalSignsEntity.getLocalDateOfVitalSign().getDayOfMonth();
        int h = vitalSignsEntity.getLocalDateOfVitalSign().getHour();
        int min = vitalSignsEntity.getLocalDateOfVitalSign().getMinute();
        when.set(y, m, d, h, min);
        vitalSigns.setEffective(new DateTimeType(when));

        Quantity quantity = new Quantity();
        quantity.setUnit(vitalSignsEntity.getUnitOfMeasurement())
                .setValue(vitalSignsEntity.getCurrentValue())
                .setSystem("http://unitsofmeasure.org")
                .setCode(vitalSignsEntity.getUnitOfMeasurement());
        vitalSigns.setValue(quantity);

        vitalSignsEntity.setAuthor(vitalSignsEntity.getAuthor());
        vitalSigns.setSubject(new Reference(vitalSignsEntity.getPatientId()));

        Extension provenance = vitalSigns.getExtensionByUrl("http://interopehrate.eu/fhir/StructureDefinition/ProvenanceExtension-IEHR");
        vitalSigns.addExtension().setValue(provenance);

// TO do  add proper subject (Patient), encounter , performer()

        return vitalSigns;
    }

    //Special designed method that changes the number of month because in FHIR protocol JANUARY is represented by 0 value (the range is from 0 to 11).
    private static int monthNumber(int monthNumber) {
        switch (monthNumber) {
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

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private Long idByAnAndSample(String an, LocalDateTime sample) {
        for (VitalSignsEntity v : this.vitalSignsRepository.findAll()) {
            if (v.getLocalDateOfVitalSign().equals(sample) && v.getAnalysisType().getName().equalsIgnoreCase(an)) {
                return v.getId();
            }
        }
        return null;
    }
}
