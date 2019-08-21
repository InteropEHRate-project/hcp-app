package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "HEALTH_CARE_ORGANIZATION")
public class HealthCareOrganizationEntity extends HCPApplicationEntity {
    @NotNull
    private String code;
    @NotNull
    private String name;
    @ManyToMany
    @JoinTable(name = "HCO_ADDRESS",
            joinColumns = @JoinColumn(name = "hco_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<AddressEntity> addresses = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "HCO_CONTACT_POINT",
            joinColumns = @JoinColumn(name = "hco_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_point_id"))
    private List<ContactPointEntity> contactPoints = new ArrayList<>();
}
