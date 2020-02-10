package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandProblem;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.ProblemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService {
    private CurrentPatient currentPatient;
    private HapiToCommandProblem hapiToCommandProblem;
    private List<ProblemInfoCommand> problemInfoCommandList = new ArrayList<>();

    public ProblemServiceImpl(CurrentPatient currentPatient, HapiToCommandProblem hapiToCommandProblem) {
        this.currentPatient = currentPatient;
        this.hapiToCommandProblem = hapiToCommandProblem;
    }

    @Override
    public ProblemCommand problemsSection() {
        var problemsList = currentPatient.conditionsList()
                .stream()
                .map(hapiToCommandProblem::convert)
                .collect(Collectors.toList());
        problemsList.addAll(problemInfoCommandList);
        return ProblemCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .problemInfoCommand(problemsList)
                .build();
    }

    @Override
    public void insertProblem(ProblemInfoCommand problemInfoCommand) {
        problemInfoCommandList.add(problemInfoCommand);
    }
}
