package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPHExam;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPHExam;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.PHExamEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class PHExamServiceImpl implements PHExamService {
    private final CurrentPatient currentPatient;
    private final PHExamRepository phExamRepository;
    private final CommandToEntityPHExam commandToEntityPHExam;
    private final EntityToCommandPHExam entityToCommandPHExam;
    private String clinicalExam;

    public PHExamServiceImpl(CurrentPatient currentPatient, PHExamRepository phExamRepository,
                             CommandToEntityPHExam commandToEntityPHExam, EntityToCommandPHExam entityToCommandPHExam) {
        this.currentPatient = currentPatient;
        this.phExamRepository = phExamRepository;
        this.commandToEntityPHExam = commandToEntityPHExam;
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
    public void insertExam(PHExamInfoCommand phExamInfoCommand) {
        this.phExamRepository.save(this.commandToEntityPHExam.convert(phExamInfoCommand));
    }

    @Override
    public PHExamInfoCommand retrieveExamById(Long id) {
        return this.entityToCommandPHExam.convert(this.phExamRepository.getOne(id));
    }

    @Override
    public void updateExam(PHExamInfoCommand phExamInfoCommand) {
        PHExamEntity phExamEntity = this.phExamRepository.getOne(phExamInfoCommand.getId());
        BeanUtils.copyProperties(phExamInfoCommand, phExamEntity);
        this.phExamRepository.save(phExamEntity);
    }

    @Override
    public void deleteExam(Long id) {
        this.phExamRepository.deleteById(id);
    }

    @Override
    public void insertClinicalExam(String clinicalExam) {
        this.clinicalExam = clinicalExam;
    }
}
