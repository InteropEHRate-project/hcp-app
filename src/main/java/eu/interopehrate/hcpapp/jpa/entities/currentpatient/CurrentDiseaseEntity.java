package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "CURRENT_DISEASES")
public class CurrentDiseaseEntity extends HCPApplicationEntity {
    private String patientId;
    @Column(name = "CODE")
    private String code;
    @NotNull
    @NotEmpty
    @Column(name = "DISEASE")
    private String disease;
    @Column(name = "DISEASE_TRANSLATED")
    private String diseaseTranslated;
    @Column(name = "DATE_OF_DIAGNOSIS")
    private LocalDate dateOfDiagnosis = LocalDate.now();
    @Column(name = "END_DATE_OF_DIAGNOSIS")
    private LocalDate endDateOfDiagnosis;
    @Column(name = "CATEGORY_CODE")
    private String categoryCode;
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @Column(name = "CLINICAL_STATUS")
    private String clinicalStatus;
    @Column(name = "VERIFICATION_STATUS")
    private String verificationStatus;
    @Column(name = "COMMENT")
    private String comment;
}
