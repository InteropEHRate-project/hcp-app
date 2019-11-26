package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexPatientDataCommand {
    private String id;
    private String lastName;
    private String firstName;
}
