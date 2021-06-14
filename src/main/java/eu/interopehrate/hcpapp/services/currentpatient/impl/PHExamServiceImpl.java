package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PHExamServiceImpl implements PHExamService {
    private final CurrentPatient currentPatient;
    private final List<PHExamInfoCommand> listOfExams = new ArrayList<>();

    public PHExamServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public List<PHExamInfoCommand> getListOfExams() {
        return listOfExams;
    }

    @Override
    public void insertExam(PHExamInfoCommand phExamInfoCommand) {
        this.listOfExams.add(phExamInfoCommand);
    }
}
