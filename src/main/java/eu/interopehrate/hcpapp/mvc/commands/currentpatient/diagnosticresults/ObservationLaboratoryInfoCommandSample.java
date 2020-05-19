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
}
