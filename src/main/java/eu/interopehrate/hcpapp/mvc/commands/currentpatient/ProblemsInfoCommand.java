package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ProblemsInfoCommand {
    @NotNull
    @NotEmpty
    private String code;
    @NotEmpty
    @NotNull
    private String name;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate onSet;
    @NotNull
    @NotEmpty
    private String categoryCode;
    @NotNull
    @NotEmpty
    private String categoryName;
    @NotNull
    @NotEmpty
    private String clinicalStatus;
    @NotNull
    @NotEmpty
    private String verificationStatus;
}
