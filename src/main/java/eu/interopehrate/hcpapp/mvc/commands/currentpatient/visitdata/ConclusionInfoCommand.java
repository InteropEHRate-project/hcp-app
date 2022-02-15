package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConclusionInfoCommand {
    private Long id;
    private String conclusionNote;
}
