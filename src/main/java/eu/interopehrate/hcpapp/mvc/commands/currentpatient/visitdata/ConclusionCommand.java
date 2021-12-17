package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConclusionCommand {
    private final CurrentDiseaseService currentDiseaseService;

    public ConclusionCommand(CurrentDiseaseService currentDiseaseService) {
        this.currentDiseaseService = currentDiseaseService;
    }
}
