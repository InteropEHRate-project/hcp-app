package eu.interopehrate.hcpapp.converters.entity;

import eu.interopehrate.hcpapp.converters.entity.utils.AddressUtils;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.mvc.commands.HealthCareProfessionalCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandHealthCareProfessional implements Converter<HealthCareProfessionalEntity, HealthCareProfessionalCommand> {
    @Override
    public HealthCareProfessionalCommand convert(HealthCareProfessionalEntity healthCareProfessionalEntity) {
        HealthCareProfessionalCommand healthCareProfessionalCommand = new HealthCareProfessionalCommand();
        healthCareProfessionalCommand.setLastName(healthCareProfessionalEntity.getLastName());
        healthCareProfessionalCommand.setFirstName(healthCareProfessionalEntity.getFirstName());
        healthCareProfessionalCommand.setAddress(AddressUtils.lastAddress(healthCareProfessionalEntity.getAddresses()));
        healthCareProfessionalCommand.setOccupationName(healthCareProfessionalEntity.getOccupation().getName());
        healthCareProfessionalCommand.setOccupationGroup(healthCareProfessionalEntity.getOccupation().getOccupationGroup().getName());
        return healthCareProfessionalCommand;
    }
}