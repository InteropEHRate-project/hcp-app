package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.AllergyEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.AllergyTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityAllergy implements Converter<AllergyInfoCommand, AllergyEntity> {

    private final AllergyTypesRepository allergyTypesRepository;

    public CommandToEntityAllergy(AllergyTypesRepository allergyTypesRepository) {
        this.allergyTypesRepository = allergyTypesRepository;
    }

    @Override
    public AllergyEntity convert(AllergyInfoCommand source) {
        AllergyEntity allergyEntity = new AllergyEntity();
        BeanUtils.copyProperties(source, allergyEntity);
        for (AllergyTypesEntity allergyTypesEntity : this.allergyTypesRepository.findAll()) {
            if (allergyTypesEntity.getName().equalsIgnoreCase(source.getName())) {
                allergyEntity.setAllergyTypesEntity(allergyTypesEntity);
                break;
            }
        }
        return allergyEntity;
    }
}
