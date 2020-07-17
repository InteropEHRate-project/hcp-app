package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Component
public class IndexPatientDataCommand {
    private String id;
    private String lastName;
    private String firstName;
    private String gender;
    private LocalDate birthDate;
    private String consent;
    private Boolean noConformantJSON = false;
    private Boolean ipsReceived = false;
    private Boolean prescriptionReceived = false;
    private Boolean laboratoryResultsReceived = false;
    private String certificate;

    public Boolean hasData() {
        return Objects.nonNull(id) || Objects.nonNull(lastName) || Objects.nonNull(firstName) || Objects.nonNull(gender) ||
                Objects.nonNull(birthDate) || Objects.nonNull(consent)||Objects.nonNull(certificate);
    }
}
