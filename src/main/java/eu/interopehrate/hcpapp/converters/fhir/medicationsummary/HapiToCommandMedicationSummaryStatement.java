package eu.interopehrate.hcpapp.converters.fhir.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryStatementCommand;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandMedicationSummaryStatement implements Converter<MedicationStatement, MedicationSummaryStatementCommand> {
    @Override
    public MedicationSummaryStatementCommand convert(MedicationStatement medicationStatement) {
        MedicationSummaryStatementCommand.MedicationSummaryStatementCommandBuilder builder = MedicationSummaryStatementCommand.builder();
        if (Objects.nonNull(medicationStatement.getMedication())) {
            builder.medication(((Reference) medicationStatement.getMedication()).getReference());
        }
        if (Objects.nonNull(medicationStatement.getStatus())) {
            builder.status(medicationStatement.getStatus().getDisplay());
        }
        if (Objects.nonNull(medicationStatement.getEffective())) {
            Date effective = ((DateTimeType) medicationStatement.getEffective()).getValue();
            builder.effective(effective.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        if (Objects.nonNull(medicationStatement.getDosage())) {
            builder.patientInstructions(medicationStatement.getDosage()
                    .stream()
                    .map(Dosage::getPatientInstruction)
                    .collect(Collectors.joining(";"))
            );
        }
        return builder.build();
    }
}
