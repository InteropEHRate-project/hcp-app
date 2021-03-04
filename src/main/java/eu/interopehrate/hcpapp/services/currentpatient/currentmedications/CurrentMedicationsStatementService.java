package eu.interopehrate.hcpapp.services.currentpatient.currentmedications;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.CurrentMedicationsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.CurrentMedicationsStatementCommand;

public interface CurrentMedicationsStatementService {
    CurrentMedicationsCommand statementCommand();
    void insertCurrentMedicationsStatement(CurrentMedicationsStatementCommand currentMedicationsStatementCommand);
}
