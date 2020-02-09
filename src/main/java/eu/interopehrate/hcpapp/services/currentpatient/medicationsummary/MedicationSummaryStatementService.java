package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;

public interface MedicationSummaryStatementService {
    MedicationSummaryCommand statementCommand();
}
