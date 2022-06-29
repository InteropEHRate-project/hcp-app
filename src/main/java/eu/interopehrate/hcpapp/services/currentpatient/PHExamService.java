package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamInfoCommand;
import org.hl7.fhir.r4.model.Condition;

import java.util.List;

public interface PHExamService {
    CurrentPatient getCurrentPatient();
    PHExamCommand phExamCommand();
    void insertClinicalExam(String clinicalExam);
    CurrentD2DConnection getCurrentD2DConnection();
    PHExamRepository getPHExamRepository();
    List<PHExamInfoCommand> getNewPhExam();
    void insertPhExam(PHExamInfoCommand phExamInfoCommand);
    Condition callPHExam();
    void delete(String phNote);
}
