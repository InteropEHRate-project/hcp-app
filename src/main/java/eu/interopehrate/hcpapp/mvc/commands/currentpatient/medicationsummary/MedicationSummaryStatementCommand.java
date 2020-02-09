package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class MedicationSummaryStatementCommand {
    private String medication;
    private String status;
    private LocalDate effective;
    private String patientInstructions;
}
