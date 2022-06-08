package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "LABORATORY_TESTS")
public class LaboratoryTestsEntity extends HCPApplicationEntity {
    private String analysisName;
    private LocalDateTime sampleDate;
    private Double currentValue;
    private String unitOfMeasurement;
    private Double upperLimitBound;
    private Double lowerLimitBound;
    private String author;
}
