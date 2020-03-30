package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareOrganizationCommand;
import org.springframework.web.multipart.MultipartFile;

public interface HealthCareOrganizationService {
    HealthCareOrganizationCommand getHealthCareOrganization();
    void saveFile(Long certificateId, MultipartFile file);
}
