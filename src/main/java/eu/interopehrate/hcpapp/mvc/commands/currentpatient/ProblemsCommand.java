package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;

import java.util.List;

@Getter
public class ProblemsCommand {
    private Boolean displayTranslatedVersion;
    private List<ProblemsInfoCommand> problemsInfoCommands;

    public ProblemsCommand(Boolean displayTranslatedVersion, List<ProblemsInfoCommand> problemsInfoCommands) {
        this.displayTranslatedVersion = displayTranslatedVersion;
        this.problemsInfoCommands = problemsInfoCommands;
    }
}
