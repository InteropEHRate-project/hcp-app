package eu.interopehrate.hcpapp.services;

import eu.interopehrate.hcpapp.mvc.commands.HealthCareOrganizationCommand;

public interface HealthCareOrganizationService {
    HealthCareOrganizationCommand getHealthCareOrganization();

    void sendInformationToSHER();
}
