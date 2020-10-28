package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatHistoryInfoCommandDiagnosis {
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
