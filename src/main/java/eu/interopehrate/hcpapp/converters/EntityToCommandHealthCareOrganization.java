package eu.interopehrate.hcpapp.converters;

import eu.interopehrate.hcpapp.converters.utils.AddressUtils;
import eu.interopehrate.hcpapp.jpa.entities.ContactPointEntity;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.ContactPointType;
import eu.interopehrate.hcpapp.mvc.commands.HealthCareOrganizationCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToCommandHealthCareOrganization implements Converter<HealthCareOrganizationEntity, HealthCareOrganizationCommand> {
    @Override
    public HealthCareOrganizationCommand convert(HealthCareOrganizationEntity healthCareOrganizationEntity) {
        HealthCareOrganizationCommand healthCareOrganizationCommand = new HealthCareOrganizationCommand();
        healthCareOrganizationCommand.setCode(healthCareOrganizationEntity.getCode());
        healthCareOrganizationCommand.setName(healthCareOrganizationEntity.getName());
        healthCareOrganizationCommand.setPhone(this.phonesAsString(healthCareOrganizationEntity.getContactPoints()));
        healthCareOrganizationCommand.setAddress(AddressUtils.lastAddress(healthCareOrganizationEntity.getAddresses()));
        return healthCareOrganizationCommand;
    }

    private String phonesAsString(List<ContactPointEntity> contactPointList) {
        return contactPointList
                .stream()
                .filter(cp -> cp.getType().equals(ContactPointType.PHONE))
                .map(ContactPointEntity::getValue)
                .collect(Collectors.joining(" / "));
    }
}
