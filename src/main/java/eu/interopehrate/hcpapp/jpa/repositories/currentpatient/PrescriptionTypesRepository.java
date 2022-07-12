package eu.interopehrate.hcpapp.jpa.repositories.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionTypesRepository extends JpaRepository<PrescriptionTypesEntity, Long> {
    List<PrescriptionTypesEntity> findAllByLang(String lang);
}
