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
@Table(name = "PH_EXAMS")
public class PHExamEntity extends HCPApplicationEntity {
    @Column(name = "ID")
    private Long id;
    @Column(name = "PATIENT_ID")
    private String patientId;
    @Column(name = "CLINICAL_EXAM")
    private String clinicalExam;
    @Column(name = "AUTHOR")
    private String author;
}
