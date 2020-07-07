package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CurrentMedicationsStatementCommand {
    @NotNull
    @NotEmpty
    private String medication;
    @NotNull
    @NotEmpty
    private String status;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate effective;
    @NotNull
    @NotEmpty
    private String patientInstructions;
}
