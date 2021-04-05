package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "VITAL_SIGNS")
public class VitalSignsEntity extends HCPApplicationEntity {
    private String patientId;
    private String author;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ANALYSIS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_TYPES"))
    private VitalSignsTypesEntity analysisType;
    private LocalDateTime localDateOfVitalSign;
    private Double currentValue;
    private String unitOfMeasurement;
}
