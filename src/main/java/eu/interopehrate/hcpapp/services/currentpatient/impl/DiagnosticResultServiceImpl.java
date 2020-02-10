package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandDiagnosticResults;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticResultService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosticResultServiceImpl implements DiagnosticResultService {
    private CurrentPatient currentPatient;
    private HapiToCommandDiagnosticResults hapiToCommandDiagnosticResults;
    private List<DiagnosticResultInfoCommand> diagnosticResultInfoCommands = new ArrayList<>();

    public DiagnosticResultServiceImpl(CurrentPatient currentPatient, HapiToCommandDiagnosticResults hapiToCommandDiagnosticResults) {
        this.currentPatient = currentPatient;
        this.hapiToCommandDiagnosticResults = hapiToCommandDiagnosticResults;
    }


    @Override
    public DiagnosticResultCommand diagnosticResultCommand() {
        var diagnosticResults = currentPatient.observationList()
                .stream()
                .map(hapiToCommandDiagnosticResults::convert)
                .collect(Collectors.toList());
        diagnosticResults.addAll(diagnosticResultInfoCommands);
        return DiagnosticResultCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .diagnosticResultInfoList(diagnosticResults)
                .build();
    }

    @Override
    public void insertDiagnosticResult(DiagnosticResultInfoCommand diagnosticResultInfoCommand) {
        diagnosticResultInfoCommands.add(diagnosticResultInfoCommand);
    }
}
