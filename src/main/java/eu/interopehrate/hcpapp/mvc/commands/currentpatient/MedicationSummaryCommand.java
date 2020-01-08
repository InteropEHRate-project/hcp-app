package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;

import java.util.List;

@Getter
public class MedicationSummaryCommand {
    private Boolean displayTranslatedVersion;
    private List<MedicationSummaryInfoCommand> medicationSummaryInfo;

    public MedicationSummaryCommand(Boolean displayTranslatedVersion, List<MedicationSummaryInfoCommand> medicationSummaryInfo) {
        this.displayTranslatedVersion = displayTranslatedVersion;
        this.medicationSummaryInfo = medicationSummaryInfo;
    }
}
