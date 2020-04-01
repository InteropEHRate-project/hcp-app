package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Getter
@Setter
@Component
public class IndexPatientDataCommand {
    private String id;
    private String lastName;
    private String firstName;
    private String consent;
    private Boolean noConformantJSON;

    public Boolean hasData() {
        return Objects.nonNull(id) || Objects.nonNull(lastName) || Objects.nonNull(firstName) || Objects.nonNull(consent);
    }
}
