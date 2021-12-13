package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPHExam;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class PHExamServiceImpl implements PHExamService {
    private final CurrentPatient currentPatient;
    private final PHExamRepository phExamRepository;
    private final EntityToCommandPHExam entityToCommandPHExam;
    private String clinicalExam;

    public PHExamServiceImpl(CurrentPatient currentPatient, PHExamRepository phExamRepository, EntityToCommandPHExam entityToCommandPHExam) {
        this.currentPatient = currentPatient;
        this.phExamRepository = phExamRepository;
        this.entityToCommandPHExam = entityToCommandPHExam;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public PHExamCommand phExamCommand() {
        var listOfExams = this.phExamRepository.findAll()
                .stream()
                .map(this.entityToCommandPHExam::convert)
                .collect(Collectors.toList());
        return PHExamCommand.builder()
                .phExamInfoCommands(listOfExams)
                .clinicalExam(this.clinicalExam)
                .build();
    }

    @Override
    public void insertClinicalExam(String clinicalExam) {
        this.clinicalExam = clinicalExam;
    }
}
