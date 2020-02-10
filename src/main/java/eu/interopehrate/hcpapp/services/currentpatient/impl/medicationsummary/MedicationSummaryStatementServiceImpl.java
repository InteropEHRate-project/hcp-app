package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandMedicationSummaryStatement;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryStatementCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryStatementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationSummaryStatementServiceImpl implements MedicationSummaryStatementService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandMedicationSummaryStatement hapiToCommandMedicationSummaryStatement;
    private final List<MedicationSummaryStatementCommand> medicationStatementCommands = new ArrayList<>();

    public MedicationSummaryStatementServiceImpl(CurrentPatient currentPatient,
                                                 HapiToCommandMedicationSummaryStatement hapiToCommandMedicationSummaryStatement) {
        this.currentPatient = currentPatient;
        this.hapiToCommandMedicationSummaryStatement = hapiToCommandMedicationSummaryStatement;
    }

    @Override
    public MedicationSummaryCommand statementCommand() {
        var medicationStatements = currentPatient.medicationStatementList()
                .stream()
                .map(hapiToCommandMedicationSummaryStatement::convert)
                .collect(Collectors.toList());
        medicationStatements.addAll(medicationStatementCommands);
        medicationStatements.sort(Comparator.comparing(MedicationSummaryStatementCommand::getMedication).reversed());
        return MedicationSummaryCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .statementList(medicationStatements)
                .build();
    }

    @Override
    public void insertMedicationSummaryStatement(MedicationSummaryStatementCommand medicationSummaryStatementCommand) {
        this.medicationStatementCommands.add(medicationSummaryStatementCommand);
    }
}
