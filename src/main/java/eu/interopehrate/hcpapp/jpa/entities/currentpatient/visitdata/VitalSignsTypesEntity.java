package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "VITAL_SIGNS_TYPES")
public class VitalSignsTypesEntity extends HCPApplicationEntity {
    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;
    @NotNull
    @NotEmpty
    @Column(name = "COMM")
    private String comm;
    @NotNull
    @NotEmpty
    @Column(name = "UCUM")
    private String ucum;
    @Column(name = "BIOPRM_RANGE")
    private String bioprm_range;
    @Column(name = "LOINC")
    private String loinc;
    @Column(name = "LANG")
    private String lang;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VitalSignsTypesEntity that = (VitalSignsTypesEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
