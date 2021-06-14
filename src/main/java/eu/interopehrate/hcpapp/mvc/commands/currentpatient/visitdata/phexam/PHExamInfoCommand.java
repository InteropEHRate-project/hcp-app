package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PHExamInfoCommand {
    private PHExamType type;
    private String result;
}
