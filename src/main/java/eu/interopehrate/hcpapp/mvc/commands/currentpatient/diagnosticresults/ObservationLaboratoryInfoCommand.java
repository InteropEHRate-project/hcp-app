package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ObservationLaboratoryInfoCommand {
    private static int index = 1;
    @NotEmpty
    @NotNull
    private String id = "f00" + index++;
    @NotEmpty
    @NotNull
    private String author = "Ion Popescu";
    @NotEmpty
    @NotNull
    private String status = "final";
    @NotEmpty
    @NotNull
    private String code = "MB";
    @NotEmpty
    @NotNull
    private LocalDate date = LocalDate.now();
    @NotEmpty
    @NotNull
    private String patient = "Mario Rossi";
    @NotEmpty
    @NotNull
    private String performer = "Spitalul Clinic de Urgenta Bagdasar-Arseni";
    @NotEmpty
    @NotNull
    private String resultsInterpreter = "-";
    @NotEmpty
    @NotNull
    private String specimen = "-";
    @NotEmpty
    @NotNull
    private String request = "-";
    @NotEmpty
    @NotNull
    private String result = "Observation/" + id;
}
