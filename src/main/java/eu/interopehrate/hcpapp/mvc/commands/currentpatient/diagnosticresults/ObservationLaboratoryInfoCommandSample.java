package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ObservationLaboratoryInfoCommandSample {
    @NotEmpty
    @NotNull
    private LocalDateTime sample = LocalDateTime.now().minusDays(2);
    @NotEmpty
    @NotNull
    private double currentValue = 0;
    @NotEmpty
    @NotNull
    private String unit = "-";
    @NotEmpty
    @NotNull
    private double upperLimitBound = Double.MAX_VALUE;
    @NotEmpty
    @NotNull
    private double lowerLimitBound = Double.MIN_VALUE;
}
