package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.ContactPointType;
import eu.interopehrate.hcpapp.jpa.entities.enums.ContactPointUse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "CONTACT_POINTS")
@EqualsAndHashCode(callSuper = true)
public class ContactPointEntity extends HCPApplicationEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContactPointType type;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContactPointUse use;
    @NotNull
    private String value;
}
