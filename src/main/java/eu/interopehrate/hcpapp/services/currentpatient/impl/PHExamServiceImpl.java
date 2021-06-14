package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PHExamServiceImpl implements PHExamService {
    private final List<PHExamInfoCommand> listOfExams = new ArrayList<>();

    @Override
    public List<PHExamInfoCommand> getListOfExams() {
        return listOfExams;
    }
}
