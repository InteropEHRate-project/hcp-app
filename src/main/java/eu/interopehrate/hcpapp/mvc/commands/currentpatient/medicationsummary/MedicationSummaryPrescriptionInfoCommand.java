package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class MedicationSummaryPrescriptionInfoCommand {
    private static int index = 1;
    @NotEmpty
    @NotNull
    private String id = "00" + index++;
    @NotEmpty
    @NotNull
    private String author = "-";
    @NotEmpty
    @NotNull
    private String status = "active";
    @NotEmpty
    @NotNull
    private LocalDate issueDate = LocalDate.now();
    @NotEmpty
    @NotNull
    private String dosageInstructions = "-";
    @NotEmpty
    @NotNull
    private String dosageInstructionsText = "-";
    @NotEmpty
    @NotNull
    private String patient = "Mario Rossi";
    @NotEmpty
    @NotNull
    private String medication = "-";
}
