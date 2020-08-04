package eu.interopehrate.hcpapp.jpa.repositories;

import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VitalSignsRepository extends JpaRepository<VitalSignsEntity, Long> {
}
