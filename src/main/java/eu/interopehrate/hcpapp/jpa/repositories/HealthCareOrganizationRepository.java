package eu.interopehrate.hcpapp.jpa.repositories;

import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCareOrganizationRepository extends JpaRepository<HealthCareOrganizationEntity, Long> {
    HealthCareOrganizationEntity findByCode(String code);
}
