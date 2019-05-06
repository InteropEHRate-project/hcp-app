package ro.siveco.europeanprojects.iehr.hcpwebapp.services;

import ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.commands.HealthCareOrganizationCommand;

public interface HealthCareOrganizationService {
    HealthCareOrganizationCommand getHealthCareOrganization();

    void sendInformationToSHER();
}
