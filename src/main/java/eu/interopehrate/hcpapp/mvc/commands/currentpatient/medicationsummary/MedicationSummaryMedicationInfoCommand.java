package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class MedicationSummaryMedicationInfoCommand {
    @NotEmpty
    @NotNull
    private String id;
    @NotEmpty
    @NotNull
    private LocalDate dateOfPrescription;
    @NotEmpty
    @NotNull
    private String drugName;
    @NotEmpty
    @NotNull
    private String drugDosage;
    @NotEmpty
    @NotNull
    private String timings;
    @NotEmpty
    @NotNull
    private String notes;
    @NotEmpty
    @NotNull
    private String status;
    @NotEmpty
    @NotNull
    private LocalDate start;
    @NotEmpty
    @NotNull
    private LocalDate end;
}
