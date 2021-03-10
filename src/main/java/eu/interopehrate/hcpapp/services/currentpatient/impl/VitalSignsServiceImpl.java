package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandVitalSigns;
import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityVitalSigns;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandVitalSigns;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.entities.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsTypesRepository;
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

    public VitalSignsServiceImpl(CurrentPatient currentPatient, HapiToCommandVitalSigns hapiToCommandVitalSigns, VitalSignsRepository vitalSignsRepository,
                                 CommandToEntityVitalSigns commandToEntityVitalSigns, EntityToCommandVitalSigns entityToCommandVitalSigns,
                                 CurrentD2DConnection currentD2DConnection, VitalSignsTypesRepository vitalSignsTypesRepository,
                                 AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.hapiToCommandVitalSigns = hapiToCommandVitalSigns;
        this.vitalSignsRepository = vitalSignsRepository;
        this.commandToEntityVitalSigns = commandToEntityVitalSigns;
        this.entityToCommandVitalSigns = entityToCommandVitalSigns;
        this.currentD2DConnection = currentD2DConnection;
        this.vitalSignsTypesRepository = vitalSignsTypesRepository;
        this.auditInformationService = auditInformationService;
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
        var vitalSignsInfoCommands = this.currentPatient.vitalSignsList()
                .stream()
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
        VitalSignsEntity vitalSignsEntity = this.commandToEntityVitalSigns.convert(vitalSignsInfoCommand);
        vitalSignsRepository.save(vitalSignsEntity);
    }

    @Override
    public void deleteVitalSign(String an, String sample) {
        LocalDateTime localDateTime = LocalDateTime.parse(sample , DateTimeFormatter.ofPattern("M/d/yy, h:mm a", Locale.US));
        for (VitalSignsEntity v : this.vitalSignsRepository.findAll()) {
            if (v.getLocalDateOfVitalSign().equals(localDateTime) && v.getAnalysisType().getName().equalsIgnoreCase(an)) {
                this.vitalSignsRepository.delete(v);
            }
        }
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

    public void callVitalSigns() {
        if (Objects.nonNull(this.currentD2DConnection.getConnectedThread())) {
            Bundle vital = new Bundle();
            vital.setEntry(new ArrayList<>());
            for (int i = 0; i < this.vitalSignsRepository.findAll().size(); i++) {
                vital.getEntry().add(new Bundle.BundleEntryComponent());
                Observation vitalSigns = createVitalSignsFromEntity(this.vitalSignsRepository.findAll().get(i));
                vital.getEntry().get(i).setResource(vitalSigns);
                this.currentPatient.getVitalSigns().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                this.currentPatient.getVitalSignsTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
            }
            this.sendVitalSigns(vital);
        } else {
            log.error("The connection with S-EHR is not established.");
        }
    }

    @SneakyThrows
    @Override
    public void sendVitalSigns(Bundle vitalSigns) {
        this.currentD2DConnection.getConnectedThread().sendVitalSigns(vitalSigns);
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
        vitalSigns.getCode().getCoding().add(new Coding());
        vitalSigns.getCode().getCoding().get(0).setDisplay(vitalSignsEntity.getAnalysisType().getName());

        Calendar when = Calendar.getInstance();
        int y = vitalSignsEntity.getLocalDateOfVitalSign().getYear();
        int m = monthNumber(vitalSignsEntity.getLocalDateOfVitalSign().getMonthValue());
        int d = vitalSignsEntity.getLocalDateOfVitalSign().getDayOfMonth();
        int h = vitalSignsEntity.getLocalDateOfVitalSign().getHour();
        int min = vitalSignsEntity.getLocalDateOfVitalSign().getMinute();
        when.set(y, m, d, h, min);
        vitalSigns.setEffective(new DateTimeType(when));

        vitalSigns.getValueQuantity().setValue(vitalSignsEntity.getCurrentValue());
        vitalSigns.getValueQuantity().setUnit(vitalSignsEntity.getUnitOfMeasurement());

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
}
