package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class IndexPatientDataCommand {
    private String id;
    private String lastName;
    private String firstName;
    private String consent;

    public Boolean hasData() {
        return Objects.nonNull(id) || Objects.nonNull(lastName) || Objects.nonNull(firstName) || Objects.nonNull(consent);
    }
}
