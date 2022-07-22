package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReasonInfoCommand {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String reason;
    private String symptom;
    @NotEmpty
    @NotNull
    private String author;
}
