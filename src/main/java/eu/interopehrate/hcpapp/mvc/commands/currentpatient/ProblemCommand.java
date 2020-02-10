package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProblemCommand {
    private Boolean displayTranslatedVersion;
    private List<ProblemInfoCommand> problemInfoCommand;
}
