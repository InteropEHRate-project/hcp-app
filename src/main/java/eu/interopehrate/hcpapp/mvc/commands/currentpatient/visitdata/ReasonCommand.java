package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReasonCommand {
    private final Boolean displayTranslatedVersion;
    private final List<ReasonInfoCommand> reasonInfoCommands;
}
