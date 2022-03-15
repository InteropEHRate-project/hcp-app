package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "DIAGNOSTIC_CONCLUSION")
public class DiagnosticConclusionEntity extends HCPApplicationEntity {
    @Column(name = "ID")
    private Long id;
    @Column(name = "PATIENT_ID")
    private String patientId;
    @NotNull
    @Column(name = "CONCLUSION_NOTE")
    private String conclusionNote;
    @NotNull
    @Column(name = "TREATMENT_PLAN")
    private String treatmentPlan;
}
