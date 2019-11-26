package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandHealthCareOrganization;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareOrganizationRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareOrganizationCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import org.springframework.stereotype.Service;

@Service
public class  HealthCareOrganizationServiceImpl implements HealthCareOrganizationService {
    private HealthCareOrganizationRepository healthCareOrganizationRepository;
    private EntityToCommandHealthCareOrganization entityToCommandHealthCareOrganization;

    public HealthCareOrganizationServiceImpl(HealthCareOrganizationRepository healthCareOrganizationRepository,
                                             EntityToCommandHealthCareOrganization entityToCommandHealthCareOrganization) {
        this.healthCareOrganizationRepository = healthCareOrganizationRepository;
        this.entityToCommandHealthCareOrganization = entityToCommandHealthCareOrganization;
    }

    @Override
    public HealthCareOrganizationCommand getHealthCareOrganization() {
        HealthCareOrganizationEntity healthCareOrganizationEntity = healthCareOrganizationRepository.findByCode("SCUBA");
        return entityToCommandHealthCareOrganization.convert(healthCareOrganizationEntity);
    }
}
