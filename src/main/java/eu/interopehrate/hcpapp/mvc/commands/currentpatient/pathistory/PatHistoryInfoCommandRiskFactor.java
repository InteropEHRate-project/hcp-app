package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatHistoryInfoCommandRiskFactor {
    @NotEmpty
    @NotNull
    private String riskFactor;
    @NotEmpty
    @NotNull
    private boolean state;
}
