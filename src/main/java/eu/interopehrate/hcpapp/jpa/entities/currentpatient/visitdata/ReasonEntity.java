package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "REASON")
public class ReasonEntity extends HCPApplicationEntity {
    @Column(name = "CARE_PLAN")
    private String carePlan;
    @Column(name = "REASON")
    private String reason;
}
