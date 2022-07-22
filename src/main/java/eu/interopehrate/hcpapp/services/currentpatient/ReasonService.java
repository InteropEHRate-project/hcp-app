package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.ReasonEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.ReasonRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ReasonInfoCommand;
import org.hl7.fhir.r4.model.Condition;

import java.util.List;

public interface ReasonService {
    List<ReasonEntity> getReasons();
    void addSymptom(String symptom);
    void delete(Long id);
    Condition callReason();
    CurrentD2DConnection getCurrentD2DConnection();
    CurrentPatient getCurrentPatient();
    ReasonRepository getReasonRepository();
    List<ReasonInfoCommand> getNewReason();
}
