package eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.PHExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PHExamRepository extends JpaRepository<PHExamEntity, Long> {
}
