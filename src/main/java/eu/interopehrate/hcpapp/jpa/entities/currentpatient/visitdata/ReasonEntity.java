package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "REASON")
public class ReasonEntity extends HCPApplicationEntity {
    private Long id;
    private String patientId;
    @NotEmpty
    @NotNull
    private String author;
    @Column(name = "REASON")
    private String reason;
}
