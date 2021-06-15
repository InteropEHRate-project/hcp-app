package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.AllergyEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityAllergy implements Converter<AllergyInfoCommand, AllergyEntity> {

    @Override
    public AllergyEntity convert(AllergyInfoCommand source) {
        AllergyEntity allergyEntity = new AllergyEntity();
        BeanUtils.copyProperties(source, allergyEntity);
        return allergyEntity;
    }
}
