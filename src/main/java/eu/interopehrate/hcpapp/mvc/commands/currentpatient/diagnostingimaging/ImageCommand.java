package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ImageCommand {
    private List<ImageInfoCommand> imageInfoCommands;
    private List<DiagnosticReportInfoCommand> diagnosticReportInfoCommands;

    public DiagnosticReportInfoCommand find(String nameDiagnostic, String dateOfReport) {
        for (DiagnosticReportInfoCommand diag : this.diagnosticReportInfoCommands) {
            if (diag.getNameDiagnostic().equals(nameDiagnostic) && diag.getDateOfReport().toString().equals(dateOfReport)) {
                return diag;
            }
        }
        return null;
    }
}
