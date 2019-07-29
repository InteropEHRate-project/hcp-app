package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "HEALTH_CARE_ORGANIZATION")
public class HealthCareOrganizationEntity extends HCPApplicationEntity {

    @NotNull
    private String code;
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String address;
}
