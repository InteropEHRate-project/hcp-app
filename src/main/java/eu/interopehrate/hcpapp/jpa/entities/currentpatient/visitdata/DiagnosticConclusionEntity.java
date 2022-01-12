package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity(name = "DIAGNOSTIC_CONCLUSION")
public class DiagnosticConclusionEntity extends HCPApplicationEntity {
    private String conclusionNote;
}
