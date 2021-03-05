package eu.interopehrate.hcpapp.jpa.repositories;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionTypesRepository extends JpaRepository<PrescriptionTypesEntity, Long> {
}
