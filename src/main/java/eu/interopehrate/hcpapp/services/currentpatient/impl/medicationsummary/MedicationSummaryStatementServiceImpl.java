package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandMedicationSummaryStatement;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryStatementCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryStatementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationSummaryStatementServiceImpl implements MedicationSummaryStatementService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandMedicationSummaryStatement hapiToCommandMedicationSummaryStatement;

    public MedicationSummaryStatementServiceImpl(CurrentPatient currentPatient,
                                                 HapiToCommandMedicationSummaryStatement hapiToCommandMedicationSummaryStatement) {
        this.currentPatient = currentPatient;
        this.hapiToCommandMedicationSummaryStatement = hapiToCommandMedicationSummaryStatement;
    }

    @Override
    public MedicationSummaryCommand statementCommand() {
        List<MedicationSummaryStatementCommand> medicationStatements = currentPatient.medicationStatementList()
                .stream()
                .map(hapiToCommandMedicationSummaryStatement::convert)
                .collect(Collectors.toList());
        return MedicationSummaryCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .statementList(medicationStatements)
                .build();
    }
}
