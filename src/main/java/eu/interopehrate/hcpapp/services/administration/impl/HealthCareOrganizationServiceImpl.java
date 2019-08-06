package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.EntityToCommandHealthCareOrganization;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareOrganizationRepository;
import eu.interopehrate.hcpapp.mvc.commands.HealthCareOrganizationCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthCareOrganizationServiceImpl implements HealthCareOrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(HealthCareOrganizationServiceImpl.class);

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

    @Override
    public void sendInformationToSHER() {
        HealthCareOrganizationEntity hco = healthCareOrganizationRepository.findByCode("SCUBA");
        logger.info("Send HCO info to SHER - {}", String.join(",", hco.getCode(), hco.getName(), hco.getPhone(), hco.getAddress()));
    }
}
