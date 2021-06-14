package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;

import java.util.List;

public interface PHExamService {
    CurrentPatient getCurrentPatient();
    List<PHExamInfoCommand> getListOfExams();
    void insertExam(PHExamInfoCommand phExamInfoCommand);
}
