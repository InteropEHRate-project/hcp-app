package eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VitalSignsInfoCommand {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String analysisName;
    @NotNull
    private VitalSignsInfoCommandSample vitalSignsInfoCommandSample = new VitalSignsInfoCommandSample();
}
