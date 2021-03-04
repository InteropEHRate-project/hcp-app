package eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AllergyIntoleranceCommand {
    private final Boolean displayTranslatedVersion;
    private final List<AllergyIntoleranceInfoCommand> allergyIntoleranceList;
}
