package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DiagnosticReportInfoCommand {
    private String nameDiagnostic;
    private String diagnosticReportType;
    private String diagnosticReportContent;
    private int sizeOfDiagnosticReport;
    private LocalDate dateOfReport;
    private String completeStringForDiagnosticReportDisplaying;
}
