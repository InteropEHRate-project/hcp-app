package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "LABORATORY_TESTS")
public class LaboratoryTestsEntity extends HCPApplicationEntity {
    private String patientId;
    private LocalDateTime sampleDate;
    private Double currentValue;
    private String unit;
    private Double upperLimitBound;
    private Double lowerLimitBound;
    private String author;
    @ManyToOne(optional = false)
    @JoinColumn(name = "LABORATORY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LABORATORY"))
    private LaboratoryTestsTypesEntity laboratoryTestsTypesEntity;
    private LocalDateTime localDateOfLaboratory;
    private String unitOfMeasurement;
}
