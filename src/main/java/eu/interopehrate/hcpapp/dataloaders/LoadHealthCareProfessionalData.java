package eu.interopehrate.hcpapp.dataloaders;

import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
public class LoadHealthCareProfessionalData {
    private HealthCareProfessionalRepository healthCareProfessionalRepository;

    public LoadHealthCareProfessionalData(HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @PostConstruct
    private void loadPicture() throws IOException {
        List<HealthCareProfessionalEntity> hcpList = healthCareProfessionalRepository.findAll();
        if (!CollectionUtils.isEmpty(hcpList)) {
            HealthCareProfessionalEntity hcp = hcpList.get(0);
            hcp.setPicture(Files.readAllBytes(new ClassPathResource("hcp-picture.png").getFile().toPath()));
            healthCareProfessionalRepository.save(hcp);
        }
    }
}
