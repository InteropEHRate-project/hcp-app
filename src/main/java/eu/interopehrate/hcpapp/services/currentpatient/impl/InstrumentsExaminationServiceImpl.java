package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityInstrumentsExam;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandInstrumentsExam;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.InstrumentsExaminationEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.InstrumentsExaminationRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.InstrumentsExaminationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class InstrumentsExaminationServiceImpl implements InstrumentsExaminationService {
    private final CurrentPatient currentPatient;
    private final InstrumentsExaminationRepository instrumentsExaminationRepository;
    private final EntityToCommandInstrumentsExam entityToCommandInstrumentsExam;
    private final CommandToEntityInstrumentsExam commandToEntityInstrumentsExam;


    public InstrumentsExaminationServiceImpl(CurrentPatient currentPatient, InstrumentsExaminationRepository instrumentsExaminationRepository, EntityToCommandInstrumentsExam entityToCommandInstrumentsExam, CommandToEntityInstrumentsExam commandToEntityInstrumentsExam) {
        this.currentPatient = currentPatient;
        this.instrumentsExaminationRepository = instrumentsExaminationRepository;
        this.entityToCommandInstrumentsExam = entityToCommandInstrumentsExam;
        this.commandToEntityInstrumentsExam = commandToEntityInstrumentsExam;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public InstrumentsExaminationCommand instrExam() {
        var listOfInstrExams = this.instrumentsExaminationRepository.findAll()
                .stream()
                .map(this.entityToCommandInstrumentsExam::convert)
                .collect(Collectors.toList());
        return InstrumentsExaminationCommand.builder()
                .instrumentsExaminationInfoCommandsExamInfoCommands(listOfInstrExams)
                .build();
    }

    @Override
    public void insertInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand) {
        this.instrumentsExaminationRepository.save(this.commandToEntityInstrumentsExam.convert(instrumentsExaminationInfoCommand));
    }

    @Override
    public InstrumentsExaminationInfoCommand retrieveInstrExamById(Long id) {
        return this.entityToCommandInstrumentsExam.convert(this.instrumentsExaminationRepository.getOne(id));
    }

    @Override
    public void updateInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand) {
        InstrumentsExaminationEntity instrumentsExaminationEntity = this.instrumentsExaminationRepository.getOne(instrumentsExaminationInfoCommand.getId());
        BeanUtils.copyProperties(instrumentsExaminationInfoCommand, instrumentsExaminationEntity);
        this.instrumentsExaminationRepository.save(instrumentsExaminationEntity);
    }

    @Override
    public void deleteInstrExam(Long id) {
        this.instrumentsExaminationRepository.deleteById(id);
    }


}
