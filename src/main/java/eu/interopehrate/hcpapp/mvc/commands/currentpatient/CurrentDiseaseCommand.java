package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CurrentDiseaseCommand {
    private Boolean displayTranslatedVersion;
    private List<CurrentDiseaseInfoCommand> currentDiseaseInfoCommand;
    private List<String> listOfNotes;
}
