package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ResultCommand {
    private final Boolean displayTranslatedVersion;
    private final List<ResultInfoCommand> resultInfoCommandList;
}
