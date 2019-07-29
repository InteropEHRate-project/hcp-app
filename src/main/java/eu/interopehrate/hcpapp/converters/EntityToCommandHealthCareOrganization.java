package eu.interopehrate.hcpapp.converters;

import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.mvc.commands.HealthCareOrganizationCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandHealthCareOrganization implements Converter<HealthCareOrganizationEntity, HealthCareOrganizationCommand> {
    @Override
    public HealthCareOrganizationCommand convert(HealthCareOrganizationEntity healthCareOrganizationEntity) {
        HealthCareOrganizationCommand healthCareOrganizationCommand = new HealthCareOrganizationCommand();
        healthCareOrganizationCommand.setCode(healthCareOrganizationEntity.getCode());
        healthCareOrganizationCommand.setName(healthCareOrganizationEntity.getName());
        healthCareOrganizationCommand.setPhone(healthCareOrganizationEntity.getPhone());
        healthCareOrganizationCommand.setAddress(healthCareOrganizationEntity.getAddress());
        return healthCareOrganizationCommand;
    }
}
