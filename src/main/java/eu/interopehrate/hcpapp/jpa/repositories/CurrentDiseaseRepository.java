package eu.interopehrate.hcpapp.jpa.repositories;

import eu.interopehrate.hcpapp.jpa.entities.CurrentDiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentDiseaseRepository extends JpaRepository<CurrentDiseaseEntity, Long> {
}
