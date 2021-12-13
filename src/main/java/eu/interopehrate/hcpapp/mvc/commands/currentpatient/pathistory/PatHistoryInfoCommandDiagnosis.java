package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDateOfDiagnosis;
}
