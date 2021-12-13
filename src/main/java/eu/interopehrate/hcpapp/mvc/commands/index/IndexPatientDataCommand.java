package eu.interopehrate.hcpapp.mvc.commands.index;

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
    private String photo;
    private String gender;
    private LocalDate birthDate;
    private String consent;
    private Boolean noConformantJSON = false;
    private Boolean ipsReceived = false;
    private Boolean prescriptionReceived = false;
    private Boolean laboratoryResultsReceived = false;
    private Boolean imageReportReceived = false;
    private Boolean patHisReceived = false;
    private Boolean vitalSignsReceived = false;
    private String certificate;
    private Integer age;
    private String country;

    public Boolean hasData() {
        return Objects.nonNull(id) || Objects.nonNull(lastName) || Objects.nonNull(firstName) || Objects.nonNull(gender) ||
                Objects.nonNull(birthDate) || Objects.nonNull(consent) || Objects.nonNull(certificate);
    }
}
