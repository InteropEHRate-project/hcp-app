package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class VitalSignsInfoCommand {
    @NotEmpty
    @NotNull
    private String analysis = "Default Analysis";

    @NotEmpty
    @NotNull
    private LocalDateTime sample = LocalDateTime.now().minusDays(2);

    @NotEmpty
    @NotNull
    private double currentValue = 0;

    @NotEmpty
    @NotNull
    private String unitOfMeasurement = "-";
}
