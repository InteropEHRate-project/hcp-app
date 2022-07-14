package eu.interopehrate.hcpapp.jpa.repositories.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.LaboratoryTestsTypesEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratoryTestsTypesRepository extends JpaRepository<LaboratoryTestsTypesEntity, Long> {
    List<LaboratoryTestsTypesEntity> findAllByLang(String lang);
}