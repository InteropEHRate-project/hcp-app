package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatHistoryInfoCommand {
    @NotEmpty
    @NotNull
    private String diagnosis;
    @NotEmpty
    @NotNull
    private int yearOfDiagnosis;
    @NotEmpty
    @NotNull
    private String comments;
}
