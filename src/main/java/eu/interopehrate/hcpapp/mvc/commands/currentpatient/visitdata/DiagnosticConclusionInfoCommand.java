package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiagnosticConclusionInfoCommand {
    private Long id;
    @NotEmpty
    @NotNull
    private String conclusionNote;
    @NotEmpty
    @NotNull
    private String treatmentPlan;
}
