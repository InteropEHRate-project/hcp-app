package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ObservationLaboratoryCommand {
    private Boolean displayTranslatedVersion;
    private List<ObservationLaboratoryInfoCommand> observationLaboratoryInfoCommands;
}
