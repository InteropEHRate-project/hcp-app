package eu.interopehrate.hcpapp.jpa.entities.administration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "AUDIT_ADMISSION_INFORMATION")
public class AdmissionDataAuditEntity extends AuditInformationEntity {
    @NotNull
    private String patientName;
    @NotNull
    private String patientId;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private String gender;
    @NotNull
    private String hospitalName;
    @NotNull
    private String hospitalId;
    @NotNull
    private String hcpName;
    @NotNull
    private String hcpId;
}
