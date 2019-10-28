package eu.interopehrate.hcpapp.mvc.commands.administration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthCareOrganizationCommand {
    private String code;
    private String name;
    private String phone;
    private String address;
}
