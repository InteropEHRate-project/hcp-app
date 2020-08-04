package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.PatHistoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.PatHistoryInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatHistoryServiceImpl implements PatHistoryService {
    private CurrentPatient currentPatient;

    //HARCODED DATA
    private PatHistoryInfoCommand patHistoryInfoCommand1 = new PatHistoryInfoCommand();
    private PatHistoryInfoCommand patHistoryInfoCommand2 = new PatHistoryInfoCommand();

    public PatHistoryServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PatHistoryCommand currentDiseasesSection() {
        List<PatHistoryInfoCommand> patHistoryInfoCommands = new ArrayList<>();
        this.patHistoryInfoCommand1.setDiagnosis("diagnosis data 1");
        this.patHistoryInfoCommand1.setYearOfDiagnosis(2013);
        this.patHistoryInfoCommand1.setComments("xxx...");
        this.patHistoryInfoCommand2.setDiagnosis("diagnosis data 2");
        this.patHistoryInfoCommand2.setYearOfDiagnosis(2017);
        this.patHistoryInfoCommand2.setComments("yyy...");
        patHistoryInfoCommands.add(this.patHistoryInfoCommand1);
        patHistoryInfoCommands.add(this.patHistoryInfoCommand2);

        return PatHistoryCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .patHistoryInfoCommands(patHistoryInfoCommands)
                .build();
    }
}
