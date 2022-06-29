package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.ReasonRepository;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.ReasonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ReasonServiceImpl implements ReasonService {
   private final ReasonRepository reasonRepository;

    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;

    public ReasonServiceImpl(ReasonRepository reasonRepository) {
        this.reasonRepository = reasonRepository;
    }

    @Override
    public List<ReasonEntity> getReasons() {
        var list = this.reasonRepository.findAll();
        Collections.sort(list);
        return list;
    }

    @Override
    public void addSymptom(String symptom) {
        ReasonEntity reasonEntity = new ReasonEntity();
        reasonEntity.setAuthor( healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " +
                healthCareProfessionalService.getHealthCareProfessional().getLastName());
        reasonEntity.setSymptom(symptom);
        if (this.reasonRepository.findAll().contains(symptom)) {
            return;
        }
        this.reasonRepository.save(reasonEntity);
    }

    @Override
    public void delete(Long id) {
        this.reasonRepository.deleteById(id);
    }
}
