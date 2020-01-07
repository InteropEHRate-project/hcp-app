package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiagnosticResultInfoCommand {
    @NotEmpty
    @NotNull
    public String nameDiagnostic;
    @NotNull
    public Double value;
    @NotEmpty
    @NotNull
    public String unitOfMeasurement;
}
