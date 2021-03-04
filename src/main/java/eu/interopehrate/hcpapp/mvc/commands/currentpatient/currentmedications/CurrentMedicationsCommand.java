package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CurrentMedicationsCommand {
    private final Boolean displayTranslatedVersion;
    private final List<CurrentMedicationsStatementCommand> statementList;
    private final List<PrescriptionCommand> prescriptionList;
    private final List<MedicationCommand> medicationList;
}
