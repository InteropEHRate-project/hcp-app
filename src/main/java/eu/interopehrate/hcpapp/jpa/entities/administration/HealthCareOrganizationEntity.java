package eu.interopehrate.hcpapp.jpa.entities.administration;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "HEALTH_CARE_ORGANIZATION")
public class HealthCareOrganizationEntity extends HCPApplicationEntity {
    @NotNull
    private String code;
    @NotNull
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "HCO_ADDRESS",
            joinColumns = @JoinColumn(name = "hco_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Set<AddressEntity> addresses = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "HCO_CONTACT_POINT",
            joinColumns = @JoinColumn(name = "hco_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_point_id"))
    private Set<ContactPointEntity> contactPoints = new HashSet<>();
    @Lob
    private Byte[] certificate;
}
