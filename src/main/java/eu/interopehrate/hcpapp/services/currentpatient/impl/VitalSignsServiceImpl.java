package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.EntityToVitalSigns;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.VitalSignsInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VitalSignsServiceImpl implements VitalSignsService {
    private CurrentPatient currentPatient;
    private List<VitalSignsInfoCommand> vitalSignsInfoCommandsList = new ArrayList<>();
    private EntityToVitalSigns entityToVitalSigns=new EntityToVitalSigns();
    @Autowired
    private VitalSignsRepository vitalSignsRepository;
    private CurrentD2DConnection currentD2DConnection;

    public VitalSignsServiceImpl(CurrentPatient currentPatient, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public VitalSignsCommand vitalSignsCommand() {
        VitalSignsInfoCommand test = new VitalSignsInfoCommand();
        test.setAnalysisName("Heart Rate");
        test.setCurrentValue(90);
        test.setUnitOfMeasurement("bpm");
        test.setLocalDateOfVitalSign(LocalDateTime.of(LocalDate.of(2020,03,12),LocalTime.of(19,20,02)));

        VitalSignsInfoCommand test2 = new VitalSignsInfoCommand();
        test2.setAnalysisName("Breathing Rate");
        test2.setCurrentValue(120);
        test2.setUnitOfMeasurement("b/min");
        test2.setLocalDateOfVitalSign(LocalDateTime.of(LocalDate.of(2020,04,12),LocalTime.of(19,20,02)));

        vitalSignsInfoCommandsList.add(test);
        vitalSignsInfoCommandsList.add(test2);
        return VitalSignsCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .vitalSignsInfoCommands(vitalSignsInfoCommandsList)
                .build();
    }

    @Override
    public void insertVitalSign(VitalSignsInfoCommand vitalSignsInfoCommand) {
        VitalSignsEntity vitalSignsEntity=this.entityToVitalSigns.convert(vitalSignsInfoCommand);
        vitalSignsRepository.save(vitalSignsEntity);
        vitalSignsInfoCommandsList.add(vitalSignsInfoCommand);


    }

    @Override
    public void sendVitalSigns(Bundle vitalSigns) throws IOException {
        this.currentD2DConnection.getConnectedThread().sendVitalSigns(vitalSigns);
        log.info("VitalSigns sent to S-EHR");
    }
}
