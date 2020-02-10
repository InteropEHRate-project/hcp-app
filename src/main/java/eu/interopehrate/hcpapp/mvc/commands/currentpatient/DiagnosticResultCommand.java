package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DiagnosticResultCommand {
    private Boolean displayTranslatedVersion;
    private List<DiagnosticResultInfoCommand> diagnosticResultInfoList;
}
