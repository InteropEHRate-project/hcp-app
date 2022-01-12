package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ConclusionCommand {
    private final CurrentDiseaseService currentDiseaseService;
    private final List<String> listOfConclusionNote = new ArrayList<>();

    public ConclusionCommand(CurrentDiseaseService currentDiseaseService) {
        this.currentDiseaseService = currentDiseaseService;
    }
}
