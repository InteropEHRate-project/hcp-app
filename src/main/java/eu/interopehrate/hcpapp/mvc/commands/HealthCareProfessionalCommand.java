package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthCareProfessionalCommand {
    private String picture;
    private String lastName;
    private String firstName;
    private String phone;
    private String address;
    private String occupationName;
    private String occupationGroup;
}
