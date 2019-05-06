package ro.siveco.europeanprojects.iehr.hcpwebapp.services.impl;

import org.springframework.stereotype.Service;
import ro.siveco.europeanprojects.iehr.hcpwebapp.converters.EntityToCommandHealthCareOrganization;
import ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.entities.HealthCareOrganizationEntity;
import ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.repositories.HealthCareOrganizationRepository;
import ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.commands.HealthCareOrganizationCommand;
import ro.siveco.europeanprojects.iehr.hcpwebapp.services.HealthCareOrganizationService;

@Service
public class HealthCareOrganizationServiceImpl implements HealthCareOrganizationService {
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
