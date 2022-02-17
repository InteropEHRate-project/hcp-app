package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ConclusionInfoCommand {
    private Long id;
    @NotEmpty
    @NotNull
    private String conclusionNote;
}
