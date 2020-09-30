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
    private final List<String> listOfPatHis = new ArrayList<>();
    private final List<String> listOfSocHis = new ArrayList<>();
    private final List<String> listOfFamHis = new ArrayList<>();

    //HARCODED DATA
    private PatHistoryInfoCommand patHistoryInfoCommand1 = new PatHistoryInfoCommand();
    private PatHistoryInfoCommand patHistoryInfoCommand2 = new PatHistoryInfoCommand();

    public PatHistoryServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PatHistoryCommand patHistorySection() {
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
                .listOfPatHis(this.listOfPatHis)
                .listOfSocHis(this.listOfSocHis)
                .listOfFamHis(this.listOfFamHis)
                .build();
    }

    @Override
    public void insertPatHis(String patHis) {
        if (patHis != null && !patHis.trim().equals("") && !this.listOfPatHis.contains(patHis)) {
            this.listOfPatHis.add(patHis);
        }
    }

    @Override
    public void deletePatHis(String patHis) {
        this.listOfPatHis.removeIf(x -> x.equals(patHis));
    }

    @Override
    public void insertSocHis(String socHis) {
        if (socHis != null && !socHis.trim().equals("") && !this.listOfSocHis.contains(socHis)) {
            this.listOfSocHis.add(socHis);
        }
    }

    @Override
    public void deleteSocHis(String socHis) {
        this.listOfSocHis.removeIf(x -> x.equals(socHis));
    }

    @Override
    public void insertFamHis(String famHis) {
        if (famHis != null && !famHis.trim().equals("") && !this.listOfFamHis.contains(famHis)) {
            this.listOfFamHis.add(famHis);
        }
    }

    @Override
    public void deleteFamHis(String famHis) {
        this.listOfFamHis.removeIf(x -> x.equals(famHis));
    }
}
