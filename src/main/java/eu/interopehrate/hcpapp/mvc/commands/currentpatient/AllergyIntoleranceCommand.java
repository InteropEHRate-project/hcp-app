package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllergyIntoleranceCommand {
    private List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfo;

    public AllergyIntoleranceCommand(List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfo) {
        this.allergyIntoleranceInfo = allergyIntoleranceInfo;
    }
}
