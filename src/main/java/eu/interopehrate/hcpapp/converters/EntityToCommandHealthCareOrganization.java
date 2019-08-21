package eu.interopehrate.hcpapp.converters;

import eu.interopehrate.hcpapp.jpa.entities.AddressEntity;
import eu.interopehrate.hcpapp.jpa.entities.ContactPointEntity;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.ContactPointType;
import eu.interopehrate.hcpapp.mvc.commands.HealthCareOrganizationCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
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
        healthCareOrganizationCommand.setAddress(this.lastAddress(healthCareOrganizationEntity.getAddresses()));
        return healthCareOrganizationCommand;
    }

    private String phonesAsString(List<ContactPointEntity> contactPointList) {
        return contactPointList
                .stream()
                .filter(cp -> cp.getType().equals(ContactPointType.PHONE))
                .map(ContactPointEntity::getValue)
                .collect(Collectors.joining(" / "));
    }

    private String lastAddress(List<AddressEntity> addressList) {
        if (CollectionUtils.isEmpty(addressList)) {
            return "";
        } else {
            addressList.sort(Comparator.comparing(AddressEntity::getCreatedDate).reversed());
            AddressEntity ae = addressList.get(0);
            return String.join(", ",
                    ae.getCity().getCountry().getName(),
                    ae.getCity().getName(),
                    ae.getStreet(),
                    ae.getNumber(),
                    ae.getDetails(),
                    ae.getPostalCode()
            );
        }
    }
}
