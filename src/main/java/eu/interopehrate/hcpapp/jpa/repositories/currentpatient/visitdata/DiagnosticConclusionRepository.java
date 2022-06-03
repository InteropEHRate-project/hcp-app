package eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticConclusionRepository extends JpaRepository<DiagnosticConclusionEntity, Long> {
}
