package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SpecimenInfoCommand {
    @NotEmpty
    @NotNull
    private String id;
    @NotEmpty
    @NotNull
    private String patientID = "-";
    @NotEmpty
    @NotNull
    private String status = "-";
    @NotEmpty
    @NotNull
    private String operator = "-";
    @NotEmpty
    @NotNull
    private String priorityOfSampling = "-";
}
