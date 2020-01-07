package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultInfoCommand;
import org.hl7.fhir.r4.model.Observation;

import java.util.List;

public interface DiagnosticResultService {
    List<DiagnosticResultInfoCommand> diagnosticResultSection();

    void insertDiagnosticResult(DiagnosticResultInfoCommand diagnosticResultInfoCommand);
}
