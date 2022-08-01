package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ImageCommand {
    private final List<ImageInfoCommand> imageInfoCommands;
    private final List<DiagnosticReportInfoCommand> diagnosticReportInfoCommands;

    public DiagnosticReportInfoCommand find(String nameDiagnostic) {
        for (DiagnosticReportInfoCommand diag : this.diagnosticReportInfoCommands) {
            if (diag.getNameDiagnostic().equals(nameDiagnostic)) {
                return diag;
            }
        }
        return null;
    }
}
