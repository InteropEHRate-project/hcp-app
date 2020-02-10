package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AllergyIntoleranceCommand {
    private Boolean displayTranslatedVersion;
    private List<AllergyIntoleranceInfoCommand> allergyIntoleranceList;
}
