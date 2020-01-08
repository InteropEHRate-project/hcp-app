package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;

import java.util.List;

@Getter
public class DiagnosticResultCommand {
    private Boolean displayTranslatedVersion;
    private List<DiagnosticResultInfoCommand> diagnosticResultInfoCommands;

    public DiagnosticResultCommand(Boolean displayTranslatedVersion, List<DiagnosticResultInfoCommand> diagnosticResultInfoCommands) {
        this.displayTranslatedVersion = displayTranslatedVersion;
        this.diagnosticResultInfoCommands = diagnosticResultInfoCommands;
    }

}
