package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PHExamInfoCommand {
    private Long id;
    private PHExamType type;
    private String result;
}
