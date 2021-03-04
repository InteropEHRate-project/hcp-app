package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MedicationCommand {
    private final Boolean displayTranslatedVersion;
    private final MedicationInfoCommand medicationInfoCommand;
}
