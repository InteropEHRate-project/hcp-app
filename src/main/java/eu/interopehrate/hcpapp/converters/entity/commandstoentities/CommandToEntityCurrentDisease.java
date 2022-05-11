package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.CurrentDiseaseTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityCurrentDisease implements Converter<CurrentDiseaseInfoCommand, CurrentDiseaseEntity> {
    private final CurrentDiseaseTypesRepository currentDiseaseTypesRepository;

    public CommandToEntityCurrentDisease(CurrentDiseaseTypesRepository currentDiseaseTypesRepository) {
        this.currentDiseaseTypesRepository = currentDiseaseTypesRepository;
    }

    @Override
    public CurrentDiseaseEntity convert(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        CurrentDiseaseEntity currentDiseaseEntity = new CurrentDiseaseEntity();
        BeanUtils.copyProperties(currentDiseaseInfoCommand, currentDiseaseEntity);
        for (CurrentDiseaseTypesEntity currentDisease : this.currentDiseaseTypesRepository.findAll()) {
            if (currentDisease.getName().equalsIgnoreCase(currentDiseaseInfoCommand.getDisease())) {
                currentDiseaseEntity.setCurrentDiseaseTypesEntity(currentDisease);
                break;
            }
        }
        return currentDiseaseEntity;
    }
}
