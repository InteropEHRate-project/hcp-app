package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MedicationCommand {
    private Boolean displayTranslatedVersion;
    private List<MedicationInfoCommand> medicationInfoCommandList;
}
