package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandDiagnosticResults;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
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
    private List<DiagnosticResultInfoCommand> diagnosticResultInfoCommandList=new ArrayList<>();

    public DiagnosticResultServiceImpl(CurrentPatient currentPatient, HapiToCommandDiagnosticResults hapiToCommandDiagnosticResults) {
        this.currentPatient = currentPatient;
        this.hapiToCommandDiagnosticResults = hapiToCommandDiagnosticResults;
    }


    @Override
    public List<DiagnosticResultInfoCommand> diagnosticResultSection() {
        List<DiagnosticResultInfoCommand> diagnosticResultList = new ArrayList<>(diagnosticResultInfoCommandList);
        diagnosticResultList.addAll(currentPatient.observationList()
                .stream()
                .map(hapiToCommandDiagnosticResults::convert)
                .collect(Collectors.toList()));
        return diagnosticResultList;
    }

    @Override
    public void insertDiagnosticResult(DiagnosticResultInfoCommand diagnosticResultInfoCommand) {
        diagnosticResultInfoCommandList.add(diagnosticResultInfoCommand);
    }
}
