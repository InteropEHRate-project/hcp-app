package eu.interopehrate.hcpapp.jpa.repositories.administration;

import eu.interopehrate.hcpapp.jpa.entities.administration.AuditInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditInformationRepository extends JpaRepository<AuditInformationEntity, Long> {
}
