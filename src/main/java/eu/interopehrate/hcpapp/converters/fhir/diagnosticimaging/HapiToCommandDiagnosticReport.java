package eu.interopehrate.hcpapp.converters.fhir.diagnosticimaging;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.DiagnosticReportInfoCommand;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Base64;

@Component
public class HapiToCommandDiagnosticReport implements Converter<DiagnosticReport, DiagnosticReportInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandDiagnosticReport(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public DiagnosticReportInfoCommand convert(DiagnosticReport source) {
        DiagnosticReportInfoCommand diagnosticReportInfoCommand = new DiagnosticReportInfoCommand();

        if (source.hasCode() && source.getCode().hasCoding()) {
            source.getCode().getCoding().forEach(coding -> diagnosticReportInfoCommand.setNameDiagnostic(CurrentPatient.extractExtensionText(coding, this.currentPatient)));
        }
        if (source.hasPresentedForm()) {
            diagnosticReportInfoCommand.setDiagnosticReportType(source.getPresentedFormFirstRep().getContentType());
            diagnosticReportInfoCommand.setDiagnosticReportContent(pictureBase64(source.getPresentedFormFirstRep().getData()));
            diagnosticReportInfoCommand.setSizeOfDiagnosticReport(source.getPresentedFormFirstRep().getSize());

            String diagnosticReportToDisplay = String.join(",", "data:" + 
                    diagnosticReportInfoCommand.getDiagnosticReportType() + ";base64", diagnosticReportInfoCommand.getDiagnosticReportContent());
            diagnosticReportInfoCommand.setCompleteStringForDiagnosticReportDisplaying(diagnosticReportToDisplay);
        }
        if (source.hasEffective() && ((DateTimeType)source.getEffective()).hasValue()) {
            diagnosticReportInfoCommand.setDateOfReport(((DateTimeType) source.getEffective()).getValue()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        return diagnosticReportInfoCommand;
    }

    private static String pictureBase64(byte[] picture) {
        return Base64.getEncoder().encodeToString(picture);
    }
}
