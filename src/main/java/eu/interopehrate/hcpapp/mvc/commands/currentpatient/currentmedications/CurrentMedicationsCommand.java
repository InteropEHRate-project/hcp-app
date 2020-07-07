package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CurrentMedicationsCommand {
    private Boolean displayTranslatedVersion;
    private List<CurrentMedicationsStatementCommand> statementList;
    private List<PrescriptionCommand> prescriptionList;
    private List<MedicationCommand> medicationList;
}
