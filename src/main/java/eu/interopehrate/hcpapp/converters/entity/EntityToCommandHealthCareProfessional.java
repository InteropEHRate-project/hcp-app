package eu.interopehrate.hcpapp.converters.entity;

import eu.interopehrate.hcpapp.converters.entity.utils.AddressUtils;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareProfessionalCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Objects;

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
        if (Objects.nonNull(healthCareProfessionalEntity.getPicture())) {
            healthCareProfessionalCommand.setPicture(this.pictureBase64(healthCareProfessionalEntity.getPicture()));
        }
        return healthCareProfessionalCommand;
    }

    private String pictureBase64(byte[] picture) {
        String base64ConnectionInfoPng = Base64.getEncoder().encodeToString(picture);
        return String.join(",", "data:image/png;base64", base64ConnectionInfoPng);
    }
}