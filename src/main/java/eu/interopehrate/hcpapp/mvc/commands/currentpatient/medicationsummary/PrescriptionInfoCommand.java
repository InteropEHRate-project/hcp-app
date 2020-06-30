package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class PrescriptionInfoCommand {
    private Long id;
    @NotEmpty
    @NotNull
    private String status;
    @NotNull
    private Integer frequency;
    @NotNull
    private Integer period;
    @NotEmpty
    @NotNull
    private String periodUnit;
    @NotEmpty
    @NotNull
    private String timings = frequency + " times per " + period + " " + periodUnit;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate start;
    @NotNull
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
    private LocalDate dateOfPrescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrescriptionInfoCommand that = (PrescriptionInfoCommand) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
