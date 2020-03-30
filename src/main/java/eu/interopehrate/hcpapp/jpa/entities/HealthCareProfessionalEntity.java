package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.PersonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "HEALTH_CARE_PROFESSIONAL")
public class HealthCareProfessionalEntity extends PersonEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "OCCUPATION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_HCP_OCCUPATION"))
    private HealthCareOccupationEntity occupation;
    @Lob
    private Byte[] certificate;
}
