package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandProblems;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.ProblemsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemsServiceImpl implements ProblemsService {
    private CurrentPatient currentPatient;
    private HapiToCommandProblems hapiToCommandProblems;
    private List<ProblemsInfoCommand> problemsInfoCommandList = new ArrayList<>();

    public ProblemsServiceImpl(CurrentPatient currentPatient, HapiToCommandProblems hapiToCommandProblems) {
        this.currentPatient = currentPatient;
        this.hapiToCommandProblems = hapiToCommandProblems;
    }

    @Override
    public ProblemsCommand problemsSection() {
        var problemsList = currentPatient.conditionsList()
                .stream()
                .map(hapiToCommandProblems::convert)
                .collect(Collectors.toList());
        problemsList.addAll(problemsInfoCommandList);
        return ProblemsCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .problemsInfoCommands(problemsList)
                .build();
    }

    @Override
    public void insertProblem(ProblemsInfoCommand problemsInfoCommand) {
        problemsInfoCommandList.add(problemsInfoCommand);
    }
}
