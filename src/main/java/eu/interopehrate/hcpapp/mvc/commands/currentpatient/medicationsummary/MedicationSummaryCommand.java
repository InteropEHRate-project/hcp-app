package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MedicationSummaryCommand {
    private Boolean displayTranslatedVersion;
    private List<MedicationSummaryStatementCommand> statementList;
    private List<PrescriptionCommand> prescriptionList;
    private List<MedicationSummaryMedicationCommand> medicationList;
}
