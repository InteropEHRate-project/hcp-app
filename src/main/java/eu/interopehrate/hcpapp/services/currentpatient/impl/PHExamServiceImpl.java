package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPHExam;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPHExam;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PHExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PHExamServiceImpl implements PHExamService {
    private final CurrentPatient currentPatient;
    private final PHExamRepository phExamRepository;
    private final EntityToCommandPHExam entityToCommandPHExam;
    private final List<String> listClinicalExam = new ArrayList<>();
    private final CurrentD2DConnection currentD2DConnection;
    private final CommandToEntityPHExam commandToEntityPHExam;

    public PHExamServiceImpl(CurrentPatient currentPatient, PHExamRepository phExamRepository, EntityToCommandPHExam entityToCommandPHExam, CurrentD2DConnection currentD2DConnection, CommandToEntityPHExam commandToEntityPHExam) {
        this.currentPatient = currentPatient;
        this.phExamRepository = phExamRepository;
        this.entityToCommandPHExam = entityToCommandPHExam;
        this.currentD2DConnection = currentD2DConnection;
        this.commandToEntityPHExam = commandToEntityPHExam;
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
                .listClinicalExam(this.listClinicalExam)
                .build();
    }

    @Override
    public void insertClinicalExam(String clinicalExam) {
        if (clinicalExam != null && !clinicalExam.trim().equals("") && !this.listClinicalExam.contains(clinicalExam)) {
            this.listClinicalExam.add(clinicalExam);
        }
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return this.currentD2DConnection;
    }

    @Override
    public PHExamRepository getPHExamRepository() {
        return phExamRepository;
    }

    @Override
    public List<PHExamInfoCommand> getNewPhExam() {
        return this.phExamRepository.findAll()
                .stream()
                .map(this.entityToCommandPHExam::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void insertPhExam(PHExamInfoCommand phExamInfoCommand) {
        this.phExamRepository.save(Objects.requireNonNull(this.commandToEntityPHExam.convert(phExamInfoCommand)));
    }
}
