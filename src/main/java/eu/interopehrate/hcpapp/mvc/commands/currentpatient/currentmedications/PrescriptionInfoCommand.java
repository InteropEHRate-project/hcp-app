package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class PrescriptionInfoCommand {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String status;
    @NotNull
    private Integer frequency;
    private Integer period = 1;
    private String periodUnit = "day";
    private String timings;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate start = LocalDate.now();

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate end;
    @NotEmpty
    @NotNull
    private String drugName;
    @NotEmpty
    @NotNull
    private String drugDosage;
    @NotEmpty
    @NotNull
    private String notes;
    private String author;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfPrescription = LocalDate.now();
}
