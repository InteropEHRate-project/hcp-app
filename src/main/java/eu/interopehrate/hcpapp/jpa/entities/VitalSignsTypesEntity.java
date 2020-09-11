package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "VITAL_SIGNS_TYPES")
public class VitalSignsTypesEntity extends HCPApplicationEntity {
    @NotNull
    @Column(name = "NAME")
    private String name;
    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;
}
