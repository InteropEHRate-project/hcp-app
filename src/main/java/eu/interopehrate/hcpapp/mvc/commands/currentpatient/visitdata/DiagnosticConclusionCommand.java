package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticConclusionService;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DiagnosticConclusionCommand {
    private final Boolean displayTranslatedVersion;
    private final CurrentDiseaseService currentDiseaseService;
    private final List<String> listOfConclusionNote;
    private final List<String> listOfTreatmentPlan;
    private final List<DiagnosticConclusionInfoCommand> diagnosticConclusionInfoCommandList;
}
