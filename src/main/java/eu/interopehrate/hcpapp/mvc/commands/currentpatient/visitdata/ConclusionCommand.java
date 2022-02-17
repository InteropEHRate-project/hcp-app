package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ConclusionCommand {
    private final Boolean displayTranslatedVersion;
    private final CurrentDiseaseService currentDiseaseService;
    private final List<String> listOfConclusionNote;
    private final List<ConclusionInfoCommand> conclusionInfoCommandList;
}
