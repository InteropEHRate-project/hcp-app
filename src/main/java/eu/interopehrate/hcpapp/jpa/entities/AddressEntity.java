package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.jpa.entities.common.PersonEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AddressUse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ADDRESSES")
public class AddressEntity extends HCPApplicationEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "CITY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ADDRESS_CITY"))
    private CityEntity city;
    @Column(name = "USE")
    @Enumerated(EnumType.STRING)
    private AddressUse use;
    @Column(name = "POSTAL_CODE")
    private String postalCode;
    @Column(name = "STREET")
    private String street;
    @Column(name = "NUMBER")
    private String number;
    @Column(name = "DETAILS")
    private String details;
    @ManyToMany(mappedBy = "addresses")
    private List<PersonEntity> persons = new ArrayList<>();
}
