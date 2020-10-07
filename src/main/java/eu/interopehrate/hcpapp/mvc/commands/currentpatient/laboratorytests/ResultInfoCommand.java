package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ResultInfoCommand {
    @NotEmpty
    @NotNull
    private String id;
    @NotEmpty
    @NotNull
    private String code = "-";
    @NotEmpty
    @NotNull
    private String value = "-";
}
