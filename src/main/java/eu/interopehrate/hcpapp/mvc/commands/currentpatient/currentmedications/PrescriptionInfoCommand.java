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
    private String idFHIR;
    private String patientId;
    @NotEmpty
    @NotNull
    private String status;
    private Integer frequency;
    private Integer period = 1;
    private String periodUnit = "day";
    private String timings;
    @NotEmpty
    @NotNull
    private String drugName;
    private String drugNameTranslated = "";
    @NotEmpty
    @NotNull
    private String drugDosage;
    @NotEmpty
    @NotNull
    private String notes;
    private String notesTranslated = "";
    private String author;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfPrescription = LocalDate.now();
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate start = LocalDate.now();
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate end;
}
