package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.CurrentMedicationsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.CurrentMedicationsStatementCommand;

public interface CurrentMedicationsStatementService {
    CurrentMedicationsCommand statementCommand();

    void insertCurrentMedicationsStatement(CurrentMedicationsStatementCommand currentMedicationsStatementCommand);
}
