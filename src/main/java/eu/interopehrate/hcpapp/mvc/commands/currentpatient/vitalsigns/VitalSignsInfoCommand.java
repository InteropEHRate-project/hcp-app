package eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VitalSignsInfoCommand {
    @NotEmpty
    @NotNull
    private String analysisName;
    @NotNull
    private VitalSignsInfoCommandSample vitalSignsInfoCommandSample = new VitalSignsInfoCommandSample();
}
