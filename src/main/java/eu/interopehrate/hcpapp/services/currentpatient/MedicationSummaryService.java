package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;

import java.util.List;

public interface MedicationSummaryService {
    MedicationSummaryCommand statementCommand();

    MedicationSummaryCommand medicationCommand();

    List<MedicationSummaryInfoCommand> medicationSummarySection();

    void insertMedicationSummary(MedicationSummaryInfoCommand medicationSummaryInfoCommand);
}
