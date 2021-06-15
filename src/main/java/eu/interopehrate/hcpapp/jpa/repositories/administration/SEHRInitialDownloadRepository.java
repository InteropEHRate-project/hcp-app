package eu.interopehrate.hcpapp.jpa.repositories.administration;

import eu.interopehrate.hcpapp.jpa.entities.administration.SEHRInitialDownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SEHRInitialDownloadRepository extends JpaRepository<SEHRInitialDownloadEntity, Long> {
}
