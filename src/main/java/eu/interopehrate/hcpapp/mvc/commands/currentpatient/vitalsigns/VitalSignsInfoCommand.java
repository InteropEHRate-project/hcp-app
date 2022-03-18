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
    private String author;
    @NotEmpty
    @NotNull
    private String analysisName;
    private String analysisNameTranslated;
    @NotNull
    private VitalSignsInfoCommandSample vitalSignsInfoCommandSample = new VitalSignsInfoCommandSample();
}
