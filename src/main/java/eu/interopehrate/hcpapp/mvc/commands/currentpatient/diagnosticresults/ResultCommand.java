package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ResultCommand {
    private Boolean displayTranslatedVersion;
    private List<ResultInfoCommand> resultInfoCommandList;
}
