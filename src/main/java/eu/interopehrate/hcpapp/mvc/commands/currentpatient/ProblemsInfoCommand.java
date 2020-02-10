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
    @NotEmpty
    @NotNull
    public String nameProblem;
    @NotNull
    @NotEmpty
    public String idProblem;
    @NotNull
    @NotEmpty
    public String code;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    public LocalDate date;
}
