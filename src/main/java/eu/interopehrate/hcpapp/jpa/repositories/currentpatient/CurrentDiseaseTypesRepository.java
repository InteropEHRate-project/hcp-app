package eu.interopehrate.hcpapp.jpa.repositories.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentDiseaseTypesRepository extends JpaRepository<CurrentDiseaseTypesEntity, Long> {
}
