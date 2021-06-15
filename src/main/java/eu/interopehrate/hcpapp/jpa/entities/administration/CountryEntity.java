package eu.interopehrate.hcpapp.jpa.entities.administration;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "COUNTRIES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ALPHA_2_CODE", name = "UK_COUNTRY_CODE_2"),
        @UniqueConstraint(columnNames = "ALPHA_3_CODE", name = "UK_COUNTRY_CODE_3"),
        @UniqueConstraint(columnNames = "NAME", name = "UK_COUNTRY_NAME")
})
public class CountryEntity extends HCPApplicationEntity {
    @NotNull
    @Column(name = "ALPHA_2_CODE")
    private String alpha2Code;
    @NotNull
    @Column(name = "ALPHA_3_CODE")
    private String alpha3Code;
    @NotNull
    @Column(name = "NAME")
    private String name;
}
