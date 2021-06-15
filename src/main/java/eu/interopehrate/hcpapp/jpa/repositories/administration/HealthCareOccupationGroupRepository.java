package eu.interopehrate.hcpapp.jpa.repositories.administration;

import eu.interopehrate.hcpapp.jpa.entities.administration.HealthCareOccupationGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCareOccupationGroupRepository extends JpaRepository<HealthCareOccupationGroupEntity, Long> {
}
