package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "PAT_HISTORY")
public class PatHistoryEntity extends HCPApplicationEntity {
    @Column(name = "ID")
    private Long id;
    @Column(name = "PATIENT_ID")
    private String patientId;
    @Column(name = "PATIENT_HISTORY")
    private String patHis;
    @Column(name = "SOCIAL_HISTORY")
    private String socHis;
    @Column(name = "FAMILY_HISTORY")
    private String famHis;
    @Column(name = "AUTHOR")
    private String author;
}
