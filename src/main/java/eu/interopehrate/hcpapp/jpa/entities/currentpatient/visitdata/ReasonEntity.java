package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity(name = "REASON")
public class ReasonEntity extends HCPApplicationEntity {
    private String carePlan;
    private String reason;
}
