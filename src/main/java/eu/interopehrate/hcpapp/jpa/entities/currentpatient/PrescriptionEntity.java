package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "PRESCRIPTION")
public class PrescriptionEntity extends HCPApplicationEntity {
    private String patientId;
    private String drugName;
    private String drugDosage;
    private Integer frequency;
    private Integer period;
    private String periodUnit;
    private String timings;
    private LocalDate start;
    private LocalDate end;
    private String status;
    private String author;
    private String notes;
    private LocalDate dateOfPrescription;
    private String patientName;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ANALYSIS_CODE", nullable = false, foreignKey = @ForeignKey(name = "FK_TYPES_CODE"))
    private PrescriptionTypesEntity prescriptionTypesEntity;
}
