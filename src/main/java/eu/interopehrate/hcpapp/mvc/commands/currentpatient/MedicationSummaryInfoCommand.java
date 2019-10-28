package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class MedicationSummaryInfoCommand {
    @NotEmpty
    @NotNull
    private String code;
    @NotEmpty
    @NotNull
    private String inn;
    @NotEmpty
    @NotNull
    private String manufacturer;
    @NotEmpty
    @NotNull
    private String concentration;
    @NotEmpty
    @NotNull
    private String dose;
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @NotEmpty
    @NotNull
    private String status;
}
