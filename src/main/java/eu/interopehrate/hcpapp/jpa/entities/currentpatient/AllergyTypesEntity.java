package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

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
@Table(name = "ALLERGY_TYPES")
public class AllergyTypesEntity extends HCPApplicationEntity {
    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;
    @Column(name = "DRUG_CLASS")
    private String drugClass;
    @Column(name = "LOINC")
    private String loinc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllergyTypesEntity that = (AllergyTypesEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
