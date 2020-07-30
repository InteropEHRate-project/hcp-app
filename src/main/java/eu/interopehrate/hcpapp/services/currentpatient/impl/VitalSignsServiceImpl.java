package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.VitalSignsInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VitalSignsServiceImpl implements VitalSignsService {
    private CurrentPatient currentPatient;
    private List<VitalSignsInfoCommand> vitalSignsInfoCommandsList = new ArrayList<>();

    public VitalSignsServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public VitalSignsCommand vitalSignsCommand() {

        List<VitalSignsInfoCommand> vitalSignsList = new ArrayList<>();
        VitalSignsInfoCommand test = new VitalSignsInfoCommand();
        test.setAnalysis("Heart Rate");
        test.setCurrentValue(90);
        test.setUnitOfMeasurement("bpm");
        test.setSample(LocalDateTime.of(LocalDate.of(2020, 03, 12), LocalTime.of(19, 20, 02)));

        vitalSignsList.add(test);
        return VitalSignsCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .vitalSignsInfoCommands(vitalSignsList)
                .build();
    }
}
