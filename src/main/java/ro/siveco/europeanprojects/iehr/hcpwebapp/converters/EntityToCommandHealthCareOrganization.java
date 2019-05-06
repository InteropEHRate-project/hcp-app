package ro.siveco.europeanprojects.iehr.hcpwebapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.entities.HealthCareOrganizationEntity;
import ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.commands.HealthCareOrganizationCommand;

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
