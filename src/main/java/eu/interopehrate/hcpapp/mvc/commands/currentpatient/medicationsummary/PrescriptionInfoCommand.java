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
    private Integer period = 1;
    private String periodUnit = "day";
    @NotEmpty
    @NotNull
    private String timings = frequency + " times per day";

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate start = LocalDate.now();

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate end = LocalDate.MAX;
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
