package eu.interopehrate.hcpapp.dataloaders;

import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class LoadHealthCareProfessionalData {
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public LoadHealthCareProfessionalData(HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @PostConstruct
    private void loadPicture() throws IOException {
        List<HealthCareProfessionalEntity> hcpList = healthCareProfessionalRepository.findAll();
        if (!CollectionUtils.isEmpty(hcpList)) {
            HealthCareProfessionalEntity hcp = hcpList.get(0);

            hcp.setPicture(FileCopyUtils.copyToByteArray(new ClassPathResource("hcp-picture.png").getInputStream()));
            healthCareProfessionalRepository.save(hcp);
        }
    }
}
