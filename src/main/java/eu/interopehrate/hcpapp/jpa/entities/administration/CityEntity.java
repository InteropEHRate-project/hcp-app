package eu.interopehrate.hcpapp.jpa.entities.administration;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "CITIES", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME", "COUNTRY_ID"}, name = "UK_CITY_NAME_COUNTRY")})
public class CityEntity extends HCPApplicationEntity {
    @NotNull
    @Column(name = "NAME")
    private String name;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", foreignKey = @ForeignKey(name = "FK_CITY_COUNTRY"))
    private CountryEntity country;
}
