package eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AllergyCommand {
    private final Boolean displayTranslatedVersion;
    private final List<AllergyInfoCommand> allergyList;
}
