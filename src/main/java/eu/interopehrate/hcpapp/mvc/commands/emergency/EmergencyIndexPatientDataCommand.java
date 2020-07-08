package eu.interopehrate.hcpapp.mvc.commands.emergency;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Component
public class EmergencyIndexPatientDataCommand {

    private String id;
    private String lastName;
    private String firstName;
    private String gender;
    private Date birthDate;
    private Boolean ipsReceived = false;

    public Boolean hasData() {
        return Objects.nonNull(id) || Objects.nonNull(lastName) || Objects.nonNull(firstName) || Objects.nonNull(gender) || Objects.nonNull(birthDate);
    }
}

