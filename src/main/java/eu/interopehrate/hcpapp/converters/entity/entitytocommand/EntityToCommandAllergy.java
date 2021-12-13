package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.AllergyEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandAllergy implements Converter<AllergyEntity, AllergyInfoCommand> {

    public AllergyInfoCommand convert(AllergyEntity allergyEntity) {
        AllergyInfoCommand allergyInfoCommand = new AllergyInfoCommand();
        BeanUtils.copyProperties(allergyEntity, allergyInfoCommand);
        return allergyInfoCommand;
    }
}
