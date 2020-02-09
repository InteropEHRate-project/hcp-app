package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryStatementCommand;

public interface MedicationSummaryStatementService {
    MedicationSummaryCommand statementCommand();

    void insertMedicationSummaryStatement(MedicationSummaryStatementCommand medicationSummaryStatementCommand);
}
