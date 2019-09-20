package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicationSummaryCommand {
    private List<MedicationSummaryInfoCommand> medicationSummaryInfo;

    public MedicationSummaryCommand(List<MedicationSummaryInfoCommand> medicationSummaryInfo) {
        this.medicationSummaryInfo = medicationSummaryInfo;
    }
}
