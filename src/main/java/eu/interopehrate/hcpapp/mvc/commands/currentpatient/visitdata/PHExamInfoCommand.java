package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PHExamInfoCommand {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String phExam;
    @NotEmpty
    @NotNull
    private String author;
}
