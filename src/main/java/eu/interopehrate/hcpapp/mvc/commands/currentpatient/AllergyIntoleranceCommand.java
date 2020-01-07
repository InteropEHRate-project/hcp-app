package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;

import java.util.List;

@Getter
public class AllergyIntoleranceCommand {
    private Boolean displayTranslatedVersion;
    private List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfo;

    public AllergyIntoleranceCommand(Boolean displayTranslatedVersion, List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfo) {
        this.displayTranslatedVersion = displayTranslatedVersion;
        this.allergyIntoleranceInfo = allergyIntoleranceInfo;
    }
}
