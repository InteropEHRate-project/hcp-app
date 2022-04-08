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
    private Long id;
    private String idFHIR;
    private String patientId;
    private String code;
    @NotEmpty
    @NotNull
    private String disease;
    private String diseaseTranslated;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfDiagnosis = LocalDate.now();
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDateOfDiagnosis;
    private String categoryCode;
    private String categoryName;
    private String clinicalStatus;
    private String verificationStatus;
    @NotNull
    @NotEmpty
    private String comment;
}
