package ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthCareOrganizationCommand {
    private String code;
    private String name;
}
