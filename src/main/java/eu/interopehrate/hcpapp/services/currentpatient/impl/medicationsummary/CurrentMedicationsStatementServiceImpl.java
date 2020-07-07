package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandMedicationSummaryStatement;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.CurrentMedicationsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.CurrentMedicationsStatementCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.CurrentMedicationsStatementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrentMedicationsStatementServiceImpl implements CurrentMedicationsStatementService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandMedicationSummaryStatement hapiToCommandMedicationSummaryStatement;
    private final List<CurrentMedicationsStatementCommand> medicationStatementCommands = new ArrayList<>();

    public CurrentMedicationsStatementServiceImpl(CurrentPatient currentPatient,
                                                  HapiToCommandMedicationSummaryStatement hapiToCommandMedicationSummaryStatement) {
        this.currentPatient = currentPatient;
        this.hapiToCommandMedicationSummaryStatement = hapiToCommandMedicationSummaryStatement;
    }

    @Override
    public CurrentMedicationsCommand statementCommand() {
        var medicationStatements = currentPatient.medicationStatementList()
                .stream()
                .map(hapiToCommandMedicationSummaryStatement::convert)
                .collect(Collectors.toList());
        medicationStatements.addAll(medicationStatementCommands);
        medicationStatements.sort(Comparator.comparing(CurrentMedicationsStatementCommand::getEffective).reversed());
        return CurrentMedicationsCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .statementList(medicationStatements)
                .build();
    }

    @Override
    public void insertCurrentMedicationsStatement(CurrentMedicationsStatementCommand currentMedicationsStatementCommand) {
        this.medicationStatementCommands.add(currentMedicationsStatementCommand);
    }
}
