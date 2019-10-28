package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandHealthCareProfessional;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareProfessionalCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class HealthCareProfessionalServiceImpl implements HealthCareProfessionalService {
    private HealthCareProfessionalRepository healthCareProfessionalRepository;
    private EntityToCommandHealthCareProfessional entityToCommandHealthCareProfessional;

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
}
