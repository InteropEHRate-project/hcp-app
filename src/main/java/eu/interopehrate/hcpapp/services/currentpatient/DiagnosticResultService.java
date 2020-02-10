package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultInfoCommand;

public interface DiagnosticResultService {
    DiagnosticResultCommand diagnosticResultCommand();

    void insertDiagnosticResult(DiagnosticResultInfoCommand diagnosticResultInfoCommand);
}
