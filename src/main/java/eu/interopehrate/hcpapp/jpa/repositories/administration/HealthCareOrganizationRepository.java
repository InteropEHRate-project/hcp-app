package eu.interopehrate.hcpapp.jpa.repositories.administration;

import eu.interopehrate.hcpapp.jpa.entities.administration.HealthCareOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCareOrganizationRepository extends JpaRepository<HealthCareOrganizationEntity, Long> {
    HealthCareOrganizationEntity findByCode(String code);
}
