package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MedicationSummaryPrescriptionCommand {
    private Boolean displayTranslatedVersion;
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand;
}
