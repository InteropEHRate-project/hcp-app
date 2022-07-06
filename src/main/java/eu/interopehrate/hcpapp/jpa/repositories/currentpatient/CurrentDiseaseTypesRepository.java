package eu.interopehrate.hcpapp.jpa.repositories.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentDiseaseTypesRepository extends JpaRepository<CurrentDiseaseTypesEntity, Long> {
    List<CurrentDiseaseTypesEntity> findAllByLang(String lang);
}
