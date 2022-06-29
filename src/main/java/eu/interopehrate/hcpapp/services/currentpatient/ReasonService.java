package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;

import java.util.List;

public interface ReasonService {
    List<ReasonEntity> getReasons();

    void addSymptom(String symptom);

    void delete(Long id);
}
