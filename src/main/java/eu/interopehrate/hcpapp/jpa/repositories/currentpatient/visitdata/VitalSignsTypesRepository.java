package eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalSignsTypesRepository extends JpaRepository<VitalSignsTypesEntity, Long> {
    List<VitalSignsTypesEntity> findAllByLang(String lang);

}
