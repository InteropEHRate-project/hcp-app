package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandHealthCareProfessional;
import eu.interopehrate.hcpapp.jpa.entities.administration.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareProfessionalCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class HealthCareProfessionalServiceImpl implements HealthCareProfessionalService {
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;
    private final EntityToCommandHealthCareProfessional entityToCommandHealthCareProfessional;

    public HealthCareProfessionalServiceImpl(HealthCareProfessionalRepository healthCareProfessionalRepository,
                                             EntityToCommandHealthCareProfessional entityToCommandHealthCareProfessional) {
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
        this.entityToCommandHealthCareProfessional = entityToCommandHealthCareProfessional;
    }

    @Override
    public HealthCareProfessionalCommand getHealthCareProfessional() {
        List<HealthCareProfessionalEntity> all = healthCareProfessionalRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return new HealthCareProfessionalCommand();
        } else {
            return entityToCommandHealthCareProfessional.convert(all.get(0));
        }
    }

    @Override
    @Transactional
    public void saveFile(Long certificateId, MultipartFile file) {
        try {
            HealthCareProfessionalEntity healthCareProfessionalEntity = healthCareProfessionalRepository.getOne(certificateId);
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }
            healthCareProfessionalEntity.setCertificate(byteObjects);

            healthCareProfessionalRepository.save(healthCareProfessionalEntity);
            log.info("Entity Certificate: " + Arrays.toString(healthCareProfessionalEntity.getCertificate()));
        } catch (IOException e) {
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }
}
