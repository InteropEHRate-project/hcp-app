package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;

import java.util.List;

public interface PHExamService {
    List<PHExamInfoCommand> getListOfExams();
}
