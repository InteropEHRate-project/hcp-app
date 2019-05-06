package ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.entities.HealthCareOrganizationEntity;

public interface HealthCareOrganizationRepository extends JpaRepository<HealthCareOrganizationEntity, Long> {
    HealthCareOrganizationEntity findByCode(String code);
}
