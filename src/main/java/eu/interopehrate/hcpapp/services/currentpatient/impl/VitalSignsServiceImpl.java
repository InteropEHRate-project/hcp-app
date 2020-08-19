package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.entity.CommandToEntityVitalSigns;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandVitalSigns;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VitalSignsServiceImpl implements VitalSignsService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandVitalSigns hapiToCommandVitalSigns;
    private List<VitalSignsInfoCommand> vitalSignsInfoCommandsList = new ArrayList<>();
    private CommandToEntityVitalSigns entityToVitalSigns = new CommandToEntityVitalSigns();
    @Autowired
    private VitalSignsRepository vitalSignsRepository;
    private CurrentD2DConnection currentD2DConnection;

    public VitalSignsServiceImpl(CurrentPatient currentPatient, HapiToCommandVitalSigns hapiToCommandVitalSigns,
                                 CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandVitalSigns = hapiToCommandVitalSigns;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return currentD2DConnection;
    }

    @Override
    public VitalSignsCommand vitalSignsCommand() throws IOException {

        File json = new ClassPathResource("VITAL_SIGN_EXAMPLE.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        Bundle vitalSignsBundle = parser.parseResource(Bundle.class, lineReadtest);

        var vitalSigns = vitalSignsBundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Observation))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Observation.class::cast)
                .collect(Collectors.toList());

        var vitalSignsInfoCommands = vitalSigns
                .stream()
                .map(hapiToCommandVitalSigns::convert)
                .collect(Collectors.toList());

        //vitalSignsInfoCommands.addAll(this.vitalSignsInfoCommandsList);
        return VitalSignsCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .vitalSignsInfoCommands(vitalSignsInfoCommands)
                .build();
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

    @Override
    public void insertVitalSigns(VitalSignsInfoCommand vitalSignsInfoCommand) {

        VitalSignsEntity vitalSignsEntity = this.entityToVitalSigns.convert(vitalSignsInfoCommand);
        vitalSignsEntity.setAnalysisName(vitalSignsInfoCommand.getAnalysisName());

        vitalSignsEntity.setLocalDateOfVitalSign(vitalSignsInfoCommand.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign());
        vitalSignsEntity.setCurrentValue(vitalSignsInfoCommand.getVitalSignsInfoCommandSample().getCurrentValue());
        vitalSignsEntity.setUnitOfMeasurement(vitalSignsInfoCommand.getVitalSignsInfoCommandSample().getUnitOfMeasurement());

        vitalSignsRepository.save(vitalSignsEntity);
        vitalSignsInfoCommandsList.add(vitalSignsInfoCommand);
    }

    @Override
    public VitalSignsCommand vitalSignsUpload() {
        return VitalSignsCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .vitalSignsInfoCommands(this.vitalSignsInfoCommandsList)
                .build();
    }

    public void callVitalSigns() throws IOException {
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

    @Override
    public void sendVitalSigns(Bundle vitalSigns) throws IOException {
        this.currentD2DConnection.getConnectedThread().sendVitalSigns(vitalSigns);
        log.info("VitalSigns sent to S-EHR");
    }

    private static Observation createVitalSignsFromEntity(VitalSignsEntity vitalSignsEntity) {
        Observation vitalSigns = new Observation();

        vitalSigns.setCode(new CodeableConcept());
        vitalSigns.getCode().addChild("coding");
        vitalSigns.getCode().setCoding(new ArrayList<>());
        vitalSigns.getCode().getCoding().add(new Coding());
        vitalSigns.getCode().getCoding().get(0).setDisplay(vitalSignsEntity.getAnalysisName());

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
}
