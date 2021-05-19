package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CurrentDiseaseInfoCommand {
    private String id;
    private String code;
    @NotEmpty
    @NotNull
    private String disease;
    private String diseaseTranslated;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfDiagnosis = LocalDate.now();
    private String categoryCode;
    private String categoryName;
    private String clinicalStatus;
    private String verificationStatus;
    @NotNull
    @NotEmpty
    private String comment;
}
