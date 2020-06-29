package eu.interopehrate.hcpapp.jpa.repositories;

import eu.interopehrate.hcpapp.jpa.entities.LaboratoryTestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratoryTestsRepository extends JpaRepository<LaboratoryTestsEntity, Long> {
}
