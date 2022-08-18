package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandHealthCareOrganization;
import eu.interopehrate.hcpapp.jpa.entities.administration.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareOrganizationRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareOrganizationCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
public class HealthCareOrganizationServiceImpl implements HealthCareOrganizationService {
    private final HealthCareOrganizationRepository healthCareOrganizationRepository;
    private final EntityToCommandHealthCareOrganization entityToCommandHealthCareOrganization;

    public HealthCareOrganizationServiceImpl(HealthCareOrganizationRepository healthCareOrganizationRepository,
                                             EntityToCommandHealthCareOrganization entityToCommandHealthCareOrganization) {
        this.healthCareOrganizationRepository = healthCareOrganizationRepository;
        this.entityToCommandHealthCareOrganization = entityToCommandHealthCareOrganization;
    }

    @Override
    public HealthCareOrganizationCommand getHealthCareOrganization() {
        HealthCareOrganizationEntity healthCareOrganizationEntity = healthCareOrganizationRepository.findByCode("HYGEIA");
        return entityToCommandHealthCareOrganization.convert(healthCareOrganizationEntity);
    }

    @Override
    @Transactional
    public void saveFile(Long certificateId, MultipartFile file) {
        try {
            HealthCareOrganizationEntity healthCareOrganizationEntity = healthCareOrganizationRepository.getOne(certificateId);
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }
            healthCareOrganizationEntity.setCertificate(byteObjects);

            healthCareOrganizationRepository.save(healthCareOrganizationEntity);
            log.info("Entity Certificate: " + Arrays.toString(healthCareOrganizationEntity.getCertificate()));
        } catch (IOException e) {
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }
}
