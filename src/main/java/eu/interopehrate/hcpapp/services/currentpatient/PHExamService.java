package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamInfoCommand;

public interface PHExamService {
    CurrentPatient getCurrentPatient();
    PHExamCommand phExamCommand();
    void insertClinicalExam(String clinicalExam);
}
