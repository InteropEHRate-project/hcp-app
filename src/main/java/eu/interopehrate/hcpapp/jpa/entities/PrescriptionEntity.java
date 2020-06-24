package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "PRESCRIPTION")
public class PrescriptionEntity extends HCPApplicationEntity {
    private String drugName;
    private String drugDosage;
    private String frequency;
    private String period;
    private String periodUnit;
    private String timings;
    private String status;
    private String author;
    private String notes;
    private LocalDate dateOfPrescription;
}
