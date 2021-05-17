package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatHistoryInfoCommandDiagnosis {
    private String id;
    @NotEmpty
    @NotNull
    private String diagnosis;
    private String diagnosisTranslated = "";
    @NotNull
    private Integer yearOfDiagnosis;
    @NotEmpty
    @NotNull
    private String comments;
}
