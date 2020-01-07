package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiagnosticResultCommand {
    private List<DiagnosticResultInfoCommand> diagnosticResultInfoCommands;

    public DiagnosticResultCommand(List<DiagnosticResultInfoCommand> diagnosticResultInfoCommands) {
        this.diagnosticResultInfoCommands = diagnosticResultInfoCommands;
    }
}
