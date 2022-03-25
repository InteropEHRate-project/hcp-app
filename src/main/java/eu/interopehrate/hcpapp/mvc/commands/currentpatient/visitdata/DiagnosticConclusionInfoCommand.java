package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiagnosticConclusionInfoCommand {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String conclusionNote;
    @NotEmpty
    @NotNull
    private String treatmentPlan;
    private String author;
}
