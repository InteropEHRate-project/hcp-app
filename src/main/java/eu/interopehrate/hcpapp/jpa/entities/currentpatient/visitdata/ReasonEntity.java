package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionTypesEntity;
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
@Table(name = "REASON")
public class ReasonEntity extends HCPApplicationEntity implements Comparable<ReasonEntity> {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String author;
    @Column(name = "SYMPTOM")
    private String symptom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasonEntity that = (ReasonEntity) o;
        return symptom.equals(that.symptom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symptom);
    }

    @Override
    public int compareTo(ReasonEntity o) {
        return Integer.compare(this.symptom.compareTo(o.getSymptom()), 0);
    }
}
