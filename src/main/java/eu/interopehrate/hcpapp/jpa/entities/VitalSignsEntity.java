package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "VITAL_SIGNS")
public class VitalSignsEntity extends HCPApplicationEntity {
    private String analysisName;
    private LocalDateTime localDateOfVitalSign;
    private Double currentValue;
    private String unitOfMeasurement;
}
