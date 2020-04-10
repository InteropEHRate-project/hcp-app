package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ObservationLaboratoryInfoCommand {
    private static int index = 1;
    @NotEmpty
    @NotNull
    private String id = Integer.toString(index++);
    @NotEmpty
    @NotNull
    private String author = "-";
    @NotEmpty
    @NotNull
    private String status = "-";
    @NotEmpty
    @NotNull
    private String code = "MB";
    @NotEmpty
    @NotNull
    private String date = "-";
    @NotEmpty
    @NotNull
    private String patient = "-";
    @NotEmpty
    @NotNull
    private String performer = "-";
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
    private String result = "-";
}
