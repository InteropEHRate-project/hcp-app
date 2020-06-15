package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class MedicationSummaryPrescriptionInfoCommand {
    private static int index = 1;
    @NotEmpty
    @NotNull
    private String id = "" + index++;
    private String author;
    @NotEmpty
    @NotNull
    private String status;
    @NotEmpty
    @NotNull
    private String timings;
    @NotEmpty
    @NotNull
    private String drugName;
    @NotEmpty
    @NotNull
    private String drugDosage;
    @NotEmpty
    @NotNull
    private String notes;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfPrescription;
}
