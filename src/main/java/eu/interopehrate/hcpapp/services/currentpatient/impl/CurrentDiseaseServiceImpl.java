package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityCurrentDisease;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandCurrentDisease;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandCurrentDisease;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.jpa.repositories.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrentDiseaseServiceImpl implements CurrentDiseaseService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandCurrentDisease hapiToCommandCurrentDisease;
    private final List<String> listOfNotes = new ArrayList<>();
    private final CommandToEntityCurrentDisease commandToEntityCurrentDisease;
    private final CurrentDiseaseRepository currentDiseaseRepository;
    private final EntityToCommandCurrentDisease entityToCommandCurrentDisease;

    public CurrentDiseaseServiceImpl(CurrentPatient currentPatient, HapiToCommandCurrentDisease hapiToCommandCurrentDisease, CommandToEntityCurrentDisease commandToEntityCurrentDisease, CurrentDiseaseRepository currentDiseaseRepository, EntityToCommandCurrentDisease entityToCommandCurrentDisease) {
        this.currentPatient = currentPatient;
        this.hapiToCommandCurrentDisease = hapiToCommandCurrentDisease;
        this.commandToEntityCurrentDisease = commandToEntityCurrentDisease;
        this.currentDiseaseRepository = currentDiseaseRepository;
        this.entityToCommandCurrentDisease = entityToCommandCurrentDisease;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public CurrentDiseaseCommand currentDiseasesSection() {
        var currentDiseasesList = this.currentPatient.conditionsList()
                .stream()
                .map(hapiToCommandCurrentDisease::convert)
                .collect(Collectors.toList());

        return CurrentDiseaseCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .currentDiseaseInfoCommand(currentDiseasesList)
                .listOfNotes(this.listOfNotes)
                .build();
    }

    @Override
    public void insertCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        currentDiseaseInfoCommand.setDisease(currentDiseaseInfoCommand.getDisease());
        CurrentDiseaseEntity currentDiseaseEntity = this.commandToEntityCurrentDisease.convert(currentDiseaseInfoCommand);
        this.currentDiseaseRepository.save(currentDiseaseEntity);
    }

    @Override
    public void insertNote(String note) {
        if (note != null && !note.trim().equals("") && !this.listOfNotes.contains(note)) {
            this.listOfNotes.add(note);
        }
    }

    @Override
    public void deleteNote(String note) {
        this.listOfNotes.removeIf(x -> x.equals(note));
    }

    @Override
    public List<CurrentDiseaseInfoCommand> listNewCurrentDiseases() {
        return this.currentDiseaseRepository.findAll()
                .stream()
                .map(this.entityToCommandCurrentDisease::convert)
                .collect(Collectors.toList());
    }
}
