package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CurrentDiseaseCommand {
    private final Boolean displayTranslatedVersion;
    private final List<CurrentDiseaseInfoCommand> currentDiseaseInfoCommand;
    private final List<String> listOfNotes;
}
