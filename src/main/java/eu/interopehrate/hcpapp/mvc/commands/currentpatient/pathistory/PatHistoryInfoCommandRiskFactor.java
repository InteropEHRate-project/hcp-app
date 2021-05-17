package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatHistoryInfoCommandRiskFactor {
    private String id;
    @NotEmpty
    @NotNull
    private String riskFactor;
    private String riskFactorTranslated = "";
    @NotEmpty
    @NotNull
    private boolean state;
}
