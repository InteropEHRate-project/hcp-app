package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
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

    @Override
    public VitalSignsCommand vitalSignsCommand() {
        List<VitalSignsInfoCommand> vitalSignsList = new ArrayList<>();
        VitalSignsInfoCommand test = new VitalSignsInfoCommand();
        test.setAnalysis("Heart Rate");
        test.setCurrentValue(90);
        test.setUnitOfMeasurement("bpm");
        test.setSample(LocalDateTime.of(LocalDate.of(2020,03,12),LocalTime.of(19,20,02)));

        VitalSignsInfoCommand test2 = new VitalSignsInfoCommand();
        test2.setAnalysis("Breathing Rate");
        test2.setCurrentValue(120);
        test2.setUnitOfMeasurement("b/min");
        test2.setSample(LocalDateTime.of(LocalDate.of(2020,04,12),LocalTime.of(19,20,02)));

        vitalSignsList.add(test);
        vitalSignsList.add(test2);
        return VitalSignsCommand.builder().vitalSignsInfoCommands(vitalSignsList).build();
    }
}
