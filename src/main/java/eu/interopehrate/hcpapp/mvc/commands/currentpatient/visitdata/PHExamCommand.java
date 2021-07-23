package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PHExamCommand {
    private final Boolean displayTranslatedVersion;
    private final List<PHExamInfoCommand> phExamInfoCommands;
    private final String clinicalExam;
}
