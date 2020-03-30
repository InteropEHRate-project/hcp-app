package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareProfessionalCommand;
import org.springframework.web.multipart.MultipartFile;

public interface HealthCareProfessionalService {
    HealthCareProfessionalCommand getHealthCareProfessional();
    void saveFile(Long certificateId, MultipartFile file);
}
