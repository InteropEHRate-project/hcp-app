package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;

public interface PHExamService {
    CurrentPatient getCurrentPatient();
    PHExamCommand phExamCommand();
    void insertExam(PHExamInfoCommand phExamInfoCommand);
    PHExamInfoCommand retrieveExamById(Long id);
    void updateExam(PHExamInfoCommand phExamInfoCommand);
    void deleteExam(Long id);
    void insertClinicalExam(String clinicalExam);
}
