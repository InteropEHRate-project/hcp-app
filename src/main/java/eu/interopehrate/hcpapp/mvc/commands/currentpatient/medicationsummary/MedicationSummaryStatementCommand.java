package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MedicationSummaryStatementCommand {
    private String medication;
    private String status;
    private LocalDate effective;
    private String patientInstructions;
}
