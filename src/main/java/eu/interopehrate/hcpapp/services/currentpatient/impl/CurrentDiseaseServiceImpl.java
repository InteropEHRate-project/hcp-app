package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandCurrentDisease;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrentDiseaseServiceImpl implements CurrentDiseaseService {
    private CurrentPatient currentPatient;
    private HapiToCommandCurrentDisease hapiToCommandCurrentDisease;
    private List<CurrentDiseaseInfoCommand> currentDiseaseInfoCommandList = new ArrayList<>();

    public CurrentDiseaseServiceImpl(CurrentPatient currentPatient, HapiToCommandCurrentDisease hapiToCommandCurrentDisease) {
        this.currentPatient = currentPatient;
        this.hapiToCommandCurrentDisease = hapiToCommandCurrentDisease;
    }

    @Override
    public CurrentDiseaseCommand currentDiseasesSection() {
        var currentDiseasesList = currentPatient.conditionsList()
                .stream()
                .map(hapiToCommandCurrentDisease::convert)
                .collect(Collectors.toList());
        currentDiseasesList.addAll(currentDiseaseInfoCommandList);
        return CurrentDiseaseCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .currentDiseaseInfoCommand(currentDiseasesList)
                .build();
    }

    @Override
    public void insertCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        currentDiseaseInfoCommandList.add(currentDiseaseInfoCommand);
    }
}
